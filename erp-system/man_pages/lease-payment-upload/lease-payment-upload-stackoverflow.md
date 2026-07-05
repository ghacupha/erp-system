# Lease payment upload stack overflow fix

## Context

Uploading lease payments from the web form triggered a `StackOverflowError` after the CSV metadata and upload record were persisted. The failure occurred while the application attempted to index the new upload in Elasticsearch. The `LeasePaymentUpload` entity holds a `CsvFileUpload` reference, and the `CsvFileUpload` entity holds a back-reference to the upload. When the search repository tried to convert this graph, the conversion service repeatedly traversed the cycle and exhausted the stack.

## Change summary

* The upload service now strips cyclical associations before calling the search repository. A lightweight copy containing only searchable fields (status, timestamps, activation flag, and the lease contract identifier) is sent to Elasticsearch.
* The relational entities remain fully populated for the transactional work and response payload.

## Impact

* Lease payment uploads can be created without runtime errors even when immediate indexing is enabled.
* Search results still include the upload status, creation time, activation state, and lease contract context, but the stored file metadata is intentionally excluded from the index to avoid recursion.

## Related files

* `src/main/java/io/github/erp/erp/leases/payments/upload/LeasePaymentUploadService.java` – prepares an index-safe copy before saving to Elasticsearch.
