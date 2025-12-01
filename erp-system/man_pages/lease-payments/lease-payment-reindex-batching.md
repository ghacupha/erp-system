# Lease payment reindex batching safeguard

## Context

Kafka-driven reindexing of lease payments occasionally carried large ID sets. The consumer forwarded the entire collection to `LeasePaymentSearchRepository.saveAll`, which triggered stack overflow errors inside the Elasticsearch client. The processing still needed to keep search results aligned with the relational updates coming from the batch upload workflow.

## Implementation highlights

- `LeasePaymentReindexConsumer` now accepts a configurable `app.search.reindex.batch-size` (default `250`) and partitions incoming payment updates into slices of that size.
- Each slice is sent to `LeasePaymentSearchRepository.saveAll` sequentially, avoiding deep call stacks while still keeping the consumer transactionally consistent.
- Debug logging records how many records were reindexed per slice to aid operations when tuning the batch size.

## Rationale

Chunking the reindex workload preserves the asynchronous search refresh while preventing the recursive stack growth observed with very large payloads. The configurable batch size lets operations tune the consumer for different cluster limits without code changes.
