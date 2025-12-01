# Lease payment upload deactivation search flow

## Context

Lease payment uploads can be deactivated to pull entire batches out of circulation. The database update must cascade to search so downstream tools stop presenting inactive payments.

## Implementation summary

- Deactivation now iterates payments for the target upload, flips their `active` flag, and saves them before completing the upload update.
- A Kafka `lease-payment-reindex` topic carries the affected payment ids post-commit. The consumer reloads the payments, enforces the active flag, and updates `LeasePaymentSearchRepository` so Elasticsearch mirrors the database state.
- Integration testing with embedded Kafka (`LeasePaymentUploadServiceIT`) confirms payments are deactivated in JPA and reindexed with `active = false` for search queries.

## Notes for operators

The reindex step is asynchronous but idempotent. If the message fails, replaying it safely re-applies the inactive flag and refreshes search without altering other fields.
