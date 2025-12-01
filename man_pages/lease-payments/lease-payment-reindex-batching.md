# Lease payment reindex batching safeguard

## Context

Large lease payment reindex messages caused stack overflow errors when the consumer pushed the entire set of entities to Elasticsearch in one call. The workflow still relies on the Kafka topic to keep the search index aligned with relational updates from batch uploads.

## Implementation highlights

- `LeasePaymentReindexConsumer` now reads a configurable `app.search.reindex.batch-size` (default `250`) to split work into smaller slices.
- Each slice is persisted sequentially through `LeasePaymentSearchRepository.saveAll`, eliminating the deep recursion that previously overflowed the stack.
- Debug logs report the slice sizes so operators can confirm batching behaviour or tune the configured size.

## Rationale

Batching the reindex requests keeps Elasticsearch consistent with the database while protecting the consumer thread from stack overflow crashes. The configuration knob keeps the solution adaptable to different deployment limits.
