# Lease payment upload stack overflow fix

## Context

Uploading lease payments from the UI resulted in a `StackOverflowError` once the upload was saved. The failure surfaced when the service attempted to push the entity to Elasticsearch because the `LeasePaymentUpload` -> `CsvFileUpload` relationship loops back to the upload. The converter cycled indefinitely through this association chain.

## Change summary

* The upload flow now creates a minimal, index-ready copy of the upload before invoking the Elasticsearch repository, preventing cyclical traversal.
* Business persistence keeps the full entity graph so the CSV metadata and lease linkage remain intact.

## Outcome

* Users can upload payment CSV files without encountering handler dispatch failures.
* Search still exposes the relevant upload metadata while deliberately omitting the file record to avoid recursion.

## Reference

* Backend adjustment: `erp-system/src/main/java/io/github/erp/erp/leases/payments/upload/LeasePaymentUploadService.java`.
