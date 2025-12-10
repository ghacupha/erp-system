# Dynamic Report Metadata Platform – Server Operations Guide

## Overview
The ERP server now ships with a configurable reporting catalogue driven by the `ReportMetadata` JPA entity. This guide documents
how the server provisions metadata, exposes REST endpoints, and supports the Angular dynamic report view. Use it as a reference
when extending the catalogue or troubleshooting production behaviour.

## Domain model
- **Entity** – `io.github.erp.domain.ReportMetadata` includes report title, module, router `pagePath`, backend API, an `active`
  toggle, and flags instructing the client to display lease period or lease contract selectors. Optional fields allow specifying
  custom query parameter names.
- **DTO and Mapper** – `ReportMetadataDTO` mirrors the entity and is mapped using `ReportMetadataMapper`. This separation keeps
  API contracts stable and simplifies validation.
- **Criteria & Query service** – `ReportMetadataCriteria` and `ReportMetadataQueryService` provide filtered lookups, enabling
  active-only listings and search endpoints without manual query code.

## Seed data lifecycle
`ReportMetadataSeederExtension` runs on application start:
1. Iterates through a curated list of lease-related report definitions.
2. For each `pagePath` the seeder either updates the existing row (when fields change) or creates a new record.
3. The `normalizeApi` helper strips leading slashes and respects fully qualified URLs, allowing metadata to point to internal or
   external APIs.
4. Saving is delegated to `ReportMetadataService`, ensuring MapStruct mappings and entity validation stay centralised.

The seed list currently provisions summaries for lease liability, right-of-use assets, service outlets, dealers, and interest
movements. Additions should follow the same pattern to maintain idempotence.

## REST endpoints
`ReportMetadataResource` exposes:
- `GET /api/erp/reporting/report-metadata` – paginated CRUD operations (standard JHipster resource).
- `GET /api/erp/reporting/report-metadata/active` – convenience method returning only active records ordered by title.
- `GET /api/erp/reporting/report-metadata/search?term=...` – case-insensitive search across title, description, module, and path.
- `GET /api/erp/reporting/report-metadata/by-path?path=...` – resolves a single metadata record by router path; used by the
  Angular summary view when navigating to `/reports/view/:slug`.

All endpoints leverage the service layer; validation and exception handling are aligned with existing REST resources. Integration
tests (`ReportMetadataResourceIT`) cover CRUD, search, and path resolution scenarios.

## Persistence and migrations
- **Liquibase** – `20240711123000_added_entity_ReportMetadata.xml` introduces the `report_metadata` table with indexes on
  `page_path` and `report_title` to support search performance.
- **Elasticsearch mock** – `ReportMetadataSearchRepositoryMockConfiguration` supports integration testing without requiring a
  live search cluster.
- **JHipster definition** – `.jhipster/ReportMetadata.json` mirrors the current entity fields so future JHipster regenerations
  respect customisations.

## Operational tips
- Keep `pagePath` values unique; they double as router destinations and lookup keys.
- The seeder runs on every start. When editing seed data for production, ensure updates are backward-compatible or adjust the
  script to preserve historical entries.
- Treat `backendApi` as a relative path unless a fully qualified URL is required. The Angular client automatically prefixes
  relative paths with the configured API base.
- When introducing additional selector toggles (e.g., depreciation book), extend the entity, update Liquibase, the DTO, mapper,
  and seeder, then coordinate with the front-end to honour the new flags.

## Troubleshooting checklist
1. **Search returns nothing** – confirm the metadata row is marked `active=true` and the search term matches title/description.
2. **Dynamic view shows “configuration not found”** – ensure the router path matches `reports/view/<slug>` and the seeder has
   executed. Re-run Liquibase migrations if the table is missing.
3. **Backend API mismatch** – verify that the `backendApi` matches a controller endpoint and that query parameters align with the
   client’s defaults or overrides.
4. **Seeder fails silently** – review logs for the `ReportMetadataSeederExtension` logger. Errors typically stem from unique
   constraint violations or validation rules; adjust the seed definitions accordingly.

Maintainers should review this guide alongside the client documentation to understand the full navigation-to-summary workflow.
