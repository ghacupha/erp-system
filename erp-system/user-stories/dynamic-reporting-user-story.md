# User Story: Maintain Dynamic Report Metadata Catalogue

## Narrative
As a platform maintainer I want to manage report metadata definitions programmatically so that new lease analytics can be
surfaced without a front-end deployment.

## Acceptance criteria
1. **Metadata persistence** – Given a JPA entity `ReportMetadata`, when I add a new seed definition the application should create
   or update the record with the expected title, description, and routing path during start-up.
2. **Search support** – When the client requests `/api/erp/reporting/report-metadata/search?term=...`, the API returns active
   records whose title, description, or path match the term.
3. **Path resolution** – When the Angular summary view calls `/by-path?path=reports/view/<slug>`, the API returns a single record
   or `404` if the path is missing.
4. **Granularity flags** – Metadata includes booleans for `displayLeasePeriod` and `displayLeaseContract`. The REST resource must
   expose these fields so the client can decide which selectors to show.
5. **Query parameter hints** – Optional `leasePeriodQueryParam` and `leaseContractQueryParam` values are passed back exactly as
   stored so backend APIs can use custom field names (e.g., `fiscalPeriodEndDate.equals`).

## Workflow trace
1. Seeder populates metadata via `ReportMetadataService.save` to respect DTO validation.
2. The REST resource offers CRUD, search, and path resolution endpoints backed by `ReportMetadataQueryService`.
3. Integration tests confirm CRUD semantics, search accuracy, and path lookups against seeded data.
4. Front-end clients use the exposed metadata to render filters and craft API queries, ensuring parity between configuration and
   runtime behaviour.
