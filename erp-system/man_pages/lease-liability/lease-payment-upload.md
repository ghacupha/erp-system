# Lease payment CSV uploads and activation flow

## Scope
This note documents the backend changes that add CSV ingestion for lease payments, plus the activation flags that control which payments participate in amortisation runs. The scope covers the new `LeasePaymentUpload` metadata entity, the batch job that materialises CSV rows, and the `active` flag on `LeasePayment` records.

## Storage and metadata
* CSV files are stored in the existing upload folder (`erp.csv-upload.storage-path`) via the shared `csvUploadFSStorageService`.
* Each upload now persists a `CsvFileUpload` record and a `LeasePaymentUpload` metadata row that ties the file to a specific IFRS16 lease contract and tracks the upload status/created timestamp.
* `LeasePaymentUpload` carries an `active` flag; deactivating an upload cascades to all related `LeasePayment` rows via `LeasePaymentRepository.updateActiveFlagForUpload`.

## Batch processing
* `LeasePaymentUploadBatchConfiguration` defines the Spring Batch job `leasePaymentUploadJob`. It reads CSV columns (`paymentDate`, `paymentAmount`, optional `active`) using the same date/decimal editors as the liability schedule import.
* The processor enriches each row with the target lease contract and upload id, defaulting `active` to `true` when omitted. The writer saves rows through `InternalLeasePaymentService.save`, which also defaults `active` to `true` when null.
* `LeasePaymentUploadJobListener` updates `uploadStatus` (`PENDING` → `PROCESSING` → `COMPLETED`/`FAILED`) and marks the `CsvFileUpload` as processed on success.

## API surface
* `POST /api/leases/lease-payment-uploads` accepts a multipart body containing the `LeasePaymentUploadRequest` (lease contract id and optional `launchBatchImmediately`) and the CSV file. It stores metadata and triggers the batch job after commit when requested.
* `GET /api/leases/lease-payment-uploads` returns mapped `LeasePaymentUploadDTO` rows (including file metadata and contract booking id) for UI visibility.
* `POST /api/leases/lease-payment-uploads/{id}/deactivate` flips the upload and all related payments to `active=false`, supporting rollback workflows when a schedule needs to be regenerated.

## Activation in amortisation
* `LeasePayment` now has an `active` flag and an optional link back to its upload. Internal queries fetching payments for amortisation (`findLeasePaymentsForLeaseContract`) filter to active rows only, ensuring stale payments are excluded from recalculations.
* Defaults are applied in both the public and internal lease payment services so ad-hoc API creations remain active unless explicitly disabled.

## Data model changes
* Liquibase changelogs create the `lease_payment_upload` table with foreign keys to `csv_file_upload` and `i_frs_16_lease_contract`, add `active`/`lease_payment_upload_id` columns to `lease_payment`, and update `.jhipster` entity definitions for regeneration safety.
