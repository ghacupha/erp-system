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
4. **Filter descriptors** – Metadata exposes a `filters` array where each element carries a label, query parameter key, value
   source, and optional UI hint so the client knows which selectors to render and which API keys to use.
5. **Composable filtering** – Multiple filter descriptors can be returned per report, and the REST payload preserves their
   declared order so the UI can present predictable layouts for leases, prepayments, and amortisation reports.

## Workflow trace
1. Seeder populates metadata via `ReportMetadataService.save` to respect DTO validation.
2. The REST resource offers CRUD, search, and path resolution endpoints backed by `ReportMetadataQueryService`.
3. Integration tests confirm CRUD semantics, search accuracy, and path lookups against seeded data.
4. Front-end clients iterate the `filters` descriptors to render selector controls and craft API queries, ensuring parity between
   configuration and runtime behaviour.
