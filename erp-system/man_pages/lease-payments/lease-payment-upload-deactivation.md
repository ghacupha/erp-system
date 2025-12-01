# Lease payment upload deactivation search flow

## Context

Disabling a lease payment upload must immediately mark the associated payments inactive in the relational store and ensure Elasticsearch queries reflect that state. Bulk updates alone left the index stale when uploads were deactivated.

## Implementation highlights

- `LeasePaymentUploadService.deactivateUpload` now loads payments for the upload, toggles the `active` flag on each record, and persists them before saving the upload metadata. A post-commit hook dispatches the affected payment ids to the Kafka topic `lease-payment-reindex` to avoid coupling index writes to the primary transaction.
- A dedicated Kafka producer/consumer pair handles reindexing: the consumer re-applies the `active` flag to the targeted payments, saves them, and refreshes `LeasePaymentSearchRepository` so search results reflect deactivation.
- Integration coverage (`LeasePaymentUploadServiceIT`) runs against embedded Kafka and a mocked search repository that captures index writes, asserting that deactivated payments are reindexed with `active = false`.

## Rationale

The asynchronous reindex keeps Elasticsearch consistent without risking transaction rollbacks from batch search updates. Persisting the payments inside the main transaction preserves the canonical database state even if the reindex message is retried.
