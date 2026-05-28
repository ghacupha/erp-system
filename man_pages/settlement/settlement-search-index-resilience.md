# Settlement Search Index Resilience

## Context

Settlement has several relationships, including invoices, labels, dealers, documents, currency and grouped settlements. Persisting the full JPA aggregate into Elasticsearch can drag a large object graph into the search index and may retain stale objects when a relational write fails or is rolled back elsewhere.

## Change

Settlement indexing now writes a shallow search document containing only the settlement's own searchable fields and a minimal settlement currency reference. The index write is dispatched after the PostgreSQL transaction commits, so a rolled-back relational save does not create a fresh search document. Search reads still use Elasticsearch for matching and ordering, but the service reloads each returned ID from PostgreSQL before mapping the response DTO.

This means stale Elasticsearch documents no longer appear in Settlement API search responses if the corresponding row is absent from PostgreSQL. The page total is based on the PostgreSQL-valid rows returned after reconciliation, not the raw Elasticsearch total. The approach follows the same direction as the lease posting rule indexing support: Elasticsearch is treated as a search aid, while PostgreSQL remains the source of truth.

## Follow-Up Pattern

For other relationship-heavy entities, prefer the same pattern:

- save a shallow indexing copy or explicit search document;
- keep to-one relationships as `{id, displayName}` style summaries where the UI needs human-readable context;
- dispatch indexing after the database transaction commits for long workflows;
- reconcile search hits against PostgreSQL before returning API responses;
- use the PostgreSQL-valid result count in API pagination metadata.
