# Lease Liability Schedule CSV Upload Pipeline

The ERP back-end now supports uploading lease liability schedules from CSV files without persisting the binary data in the database. The flow mirrors the documentation in `erp-system/man-pages/lease-liability/lease-liability-schedule-csv-upload.md` and can be summarised as follows:

1. A multipart request to `/api/leases/lease-liability-schedule-file-uploads` stores the file on disk via `CsvUploadFSStorageService` while persisting metadata in `CsvFileUpload` and `LeaseLiabilityScheduleFileUpload`.
2. A Spring Batch job (`leaseLiabilityScheduleUploadJob`) reads the CSV, validates mandatory columns, enriches each row with the lease identifiers provided in the request, and publishes the items to the Kafka topic `lease-liability-schedule-items`.
3. `LeaseLiabilityScheduleItemKafkaConsumer` consumes the messages, resolves the referenced aggregates (lease liability, amortisation schedule, compilation, repayment period), and saves `LeaseLiabilityScheduleItem` entities.
4. Upload status is tracked on the `LeaseLiabilityScheduleFileUpload` record (`PENDING` → `PROCESSING` → `COMPLETED`/`FAILED`).

Liquibase changelog files `20240704120000_added_entity_CsvFileUpload.xml` and `20240704120100_added_entity_LeaseLiabilityScheduleFileUpload.xml` create the supporting tables and enforce the one-to-one relationship between upload metadata and the CSV descriptor.
