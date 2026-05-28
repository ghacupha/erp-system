# Shallow Search Indexing Rollout

## Decision

Elasticsearch should not receive full JPA aggregates for relationship-heavy entities. Search documents should contain the entity's own searchable fields plus minimal to-one relationship summaries, normally `{id, displayName}`. PostgreSQL remains the source of truth.

## Rules

- Save and delete search documents after the database transaction commits.
- Do not index to-many collections unless a purpose-built search document explicitly needs a small denormalised summary.
- Startup batch indexers prepare entities through a generic shallow document sanitizer before calling `saveAll`.
- To-one relationships are indexed as same-type summaries containing `id` plus common display fields such as name, code, number, currency name, login, or email.
- Reconcile search hits against PostgreSQL before returning API DTOs.
- Report page totals from PostgreSQL-valid reconciled results, not raw Elasticsearch totals.
- Migrate generated services gradually by risk tier instead of editing hundreds of services in one pass.

## Rollout Order

1. Add or reuse central helpers for after-commit indexing and reconciliation.
2. Convert high-risk aggregate roots first: settlements, invoices, payments, business documents, WIP records and lease workflows.
3. Use the startup batch indexer sanitizer for entities that already extend the v2 batch base class.
4. Migrate older v1 indexers to call the shared preparation hook when each service is reviewed.
5. Keep simple master-data entities on generated indexing unless they show relationship or stale-index failures.
6. Rebuild affected indices after each entity group is converted.
