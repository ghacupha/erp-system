# Lease Liability Compilation Search Index Flattening

## Summary
The Spring Data Elasticsearch mapping for `LeaseLiabilityCompilation` previously attempted to persist the entire JPA aggregate. Nested relationships between `LeaseLiabilityCompilation`, `LeaseLiabilityScheduleItem`, `LeaseLiability`, and amortisation entities formed a cyclic graph. When the Elasticsearch converter traversed this graph it repeatedly serialised the same nodes until the JVM exhausted the stack, triggering `StackOverflowError` whenever a compilation was indexed.

## Implementation
- Introduced `LeaseLiabilityCompilationSearchDocument`, a lightweight document that contains only scalar compilation metadata and a trimmed requester summary. The document is stored in the dedicated `leaseliabilitycompilation-search` index.
- Added `LeaseLiabilityCompilationSearchMapper` to translate between the rich domain model, the flattened search document, and DTOs returned by search endpoints. The mapper copies essential fields and shields Elasticsearch from recursive associations.
- Updated both the public (`LeaseLiabilityCompilationServiceImpl`) and internal (`InternalLeaseLiabilityCompilationServiceImpl`) services, as well as batch helpers, to index the flattened document instead of the JPA entity.
- Adjusted the custom search repository to operate on the flattened document type and amended integration tests to work with the new projection.

## Operational Notes
- Existing API contracts remain unchanged because search responses are rehydrated into `LeaseLiabilityCompilationDTO` instances before leaving the service layer.
- The flattened document intentionally omits schedule items and other heavy associations. If search needs additional attributes in the future, extend the document and mapper deliberately rather than reintroducing entity graphs.
- Rebuild the Elasticsearch index (or allow it to auto-create on next save) after deploying the change so that old, cyclicly-mapped entities are replaced by the new flat representation.
