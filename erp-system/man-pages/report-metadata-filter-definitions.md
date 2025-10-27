# Report Metadata Filter Definitions

## Overview
Dynamic reports now expose a list of filter descriptors through the `filters` attribute on `ReportMetadata` REST payloads. Each descriptor provides:

- A human friendly `label` for the control caption.
- A `queryParameterKey` that can be sent to backend reporting APIs.
- A `valueSource` token indicating which catalogue the UI should query for options.
- An optional `uiHint` to steer rendering (e.g., `dropdown`, `typeahead`).

## Workflow impact
1. The seeder populates lease, prepayment, and amortisation reports with baseline filter descriptors during application start-up.
2. REST consumers retrieve descriptors from `/api/report-metadata` or the search endpoint and render controls in order.
3. Filter selections are translated into backend requests by concatenating the `queryParameterKey` with chosen values.
4. Integration tests validate CRUD and search flows against the new structure.

## Related components
- `ReportMetadata` entity now stores filter descriptors via an `@ElementCollection` mapping.
- Liquibase changelog `20240801090000_update_report_metadata_filters.xml` updates the schema.
- `ReportMetadataSeederExtension` seeds descriptors for leases, prepayments, and amortisation reports.
- `ReportMetadataResourceIT` verifies JSON payloads and persistence of filter definitions.
