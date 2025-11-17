# Lease Liability Schedule CSV Upload Pipeline

## Overview
The lease liability schedule upload feature decouples raw CSV files from the database by storing the binary data on disk and persisting lightweight metadata that is consumed by a Spring Batch job. Each upload produces Kafka messages that are consumed asynchronously and persisted as `LeaseLiabilityScheduleItem` entities.

## Storage and Metadata
1. The REST endpoint `/api/leases/lease-liability-schedule-file-uploads` accepts a multipart form request containing:
   - A JSON payload with the lease liability identifiers (`leaseLiabilityId`, `leaseAmortizationScheduleId`, `leaseLiabilityCompilationId`).
   - The CSV file itself.
2. `CsvUploadFSStorageService` writes the CSV to `${erp.csv-upload.storage-path}`. The default location is `${java.io.tmpdir}/erp/csv-uploads` and can be overridden through environment variables.
3. The controller delegates to `LeaseLiabilityScheduleUploadService` which persists two entities:
   - `CsvFileUpload`: stores the file path, checksum, and metadata needed for traceability.
   - `LeaseLiabilityScheduleFileUpload`: links the CSV metadata to the lease liability context and tracks processing status.
4. The service immediately launches the Spring Batch job (unless explicitly disabled in the request).

## Batch Job
`LeaseLiabilityScheduleBatchJobConfiguration` wires the processing pipeline:
- **Reader:** `FlatFileItemReader<RowItem<LeaseLiabilityScheduleItemQueueItem>>` maps each CSV row to a queue item and keeps the original row number for diagnostics.
- **Processors:** validation checks for the sequence number and ensures either a repayment-period id or a payment date is supplied. A dedicated resolver then looks up the repayment period whose date range contains the payment date (via `InternalLeaseRepaymentPeriodRepository`) before the metadata processor enriches the item with liability, compilation, and upload identifiers.
- **Writer:** publishes each enriched item to the Kafka topic `lease-liability-schedule-items` using the `leaseLiabilityScheduleKafkaTemplate`.
- **Listeners:**
  - `LeaseLiabilityScheduleUploadJobListener` updates the upload status (`PENDING` → `PROCESSING` → `COMPLETED`/`FAILED`).
  - Standard persistence listener logs execution metrics.

## Kafka Integration
- Producer configuration lives in `LeaseLiabilityScheduleKafkaConfig`, which exposes a dedicated `KafkaTemplate` and listener factory.
- Messages contain all numeric balances plus the lease identifiers. They are keyed by a generated UUID to preserve ordering while allowing parallelism.

## Consumer and Persistence
`LeaseLiabilityScheduleItemKafkaConsumer` listens on the same topic and converts each queue item into a `LeaseLiabilityScheduleItem` entity. It resolves related aggregates (lease liability, amortization schedule, repayment period, compilation) before saving to the repository.

## Database Schema
Liquibase changelog files create two new tables:
- `csv_file_upload` for disk-backed file metadata (with unique constraints on stored file name and path).
- `lease_liability_schedule_file_upload` for processing metadata and the foreign key to the CSV entry.

## Operational Notes
- The upload status defaults to `PENDING`, moves to `PROCESSING` when the batch starts, and is marked `COMPLETED` if the job exits successfully, `FAILED` otherwise.
- CSV parsing strips thousands separators and supports ISO or `dd/MM/yyyy` dates. The CSV header now includes `paymentDate` so a row can be matched to the correct repayment period even when the numeric identifier is not known.
- Errors during batch processing are logged via the skip listener; problematic rows are skipped without halting the entire job.
