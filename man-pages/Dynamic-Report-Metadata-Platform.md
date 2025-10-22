# Dynamic Report Metadata Platform – Case Study

## Purpose and scope
The ERP reporting catalogue has outgrown individually crafted Angular components. The new dynamic reporting platform enables
administrators and analysts to expose dozens of lease-focused summaries by configuring metadata instead of duplicating UI code.
This document explains the end-to-end workflow – from metadata seeding to the responsive summary view – and the operational
considerations for supporting the new experience.

## Architectural overview
1. **Metadata entity** – `ReportMetadata` captures the report title, business module, navigation slug (`pagePath`), backend API,
and feature toggles (`displayLeasePeriod`, `displayLeaseContract`). Two optional fields allow developers to override the query
parameter names expected by specialised endpoints.
2. **Seeder extension** – `ReportMetadataSeederExtension` populates the repository at application start-up with curated lease
reports (liability by account, ROU balances, interest payments, etc.). The seeder is idempotent and updates existing rows when
configuration changes.
3. **REST surface** – `ReportMetadataResource` exposes:
   - `GET /api/erp/reporting/report-metadata/active` – list of active reports.
   - `GET /search` – full-text search by title/description for navigation quick find.
   - `GET /by-path` – resolve a metadata record by its router path.
4. **Client application** – the Angular package introduces:
   - `ReportMetadataService` for search/lookup calls.
   - `ReportSummaryDataService` that dereferences the configured backend API, applies filter parameters and normalises the
     response into a table-friendly structure.
   - `ReportSummaryViewComponent` – a generic component that renders metadata headings, optional filter pickers, and a dynamic
     results table.
   - Navbar integration that exposes a global search bar for report discovery.

## End-to-end workflow
1. **User searches for a report** from the navigation bar. Keystrokes trigger a debounced call to `ReportMetadataService.search`
   that returns active metadata rows. The dropdown surfaces the title, description, and slug, allowing immediate navigation.
2. **Router navigation** directs the user to `/reports/view/:slug`. Lazy loading resolves the metadata using
   `ReportSummaryViewComponent`, which calls `ReportMetadataService.findByPagePath('reports/view/<slug>')`.
3. **Filter initialisation**
   - If `displayLeasePeriod` is true, the component loads the most recent lease periods and selects the latest entry.
   - If `displayLeaseContract` is true, the component loads available lease liabilities and defaults to the newest item.
   - Query parameter overrides (`leasePeriodQueryParam`, `leaseContractQueryParam`) allow APIs to expect date-based filters or
     alternative identifiers.
4. **Summary retrieval** – `ReportSummaryDataService` calls the configured backend API with a standard `size` parameter and the
   optional filters. Responses are treated as `Record<string, unknown>` rows; the component derives column headings dynamically
   from the payload.
5. **Presentation** – the summary view displays:
   - Report title and marketing description.
   - Filter pickers with spinner feedback during asynchronous loads.
   - Warnings when no metadata exists, the endpoint is missing, or the API returns no results.
   - A responsive table that adapts to the varying key sets returned by different reports.

## Extending the catalogue
1. **Add metadata** – update `ReportMetadataSeederExtension` (or a similar configuration source) with the new report definition.
   Ensure the backend API exists and supports the expected filters.
2. **Liquibase & DTO synchronisation** – changes to the entity must be reflected in `.jhipster/ReportMetadata.json`, DTOs,
   service/criteria classes, and Liquibase changelog files. The current changelog (`20240711123000_added_entity_ReportMetadata.xml`)
   includes the required columns and indexes for search.
3. **Document the workflow** – extend this case study, the user stories, and the SQL appendix so operators can trace the full
   journey from UI to data warehouse.
4. **Optional UI enhancements** – provide additional selectors (e.g., depreciation books) by following the existing pattern:
   update the metadata with a `displayX` toggle, add a matching service injection, and append the new query parameter to the
   request map.

## Operational considerations
- **Search performance** – the search API is backed by the JPA repository and Elasticsearch mock. Keep titles concise and include
  domain-specific keywords to make fuzzy matching intuitive.
- **Security** – router entries in `ErpReportsModule` require `ROLE_DEV`, `ROLE_REPORT_ACCESSOR`, or `ROLE_REPORT_DESIGNER`. Update
  authority lists if specific reports should be restricted to other roles.
- **Error handling** – the summary component preserves error messages from metadata, filter loads, and the summary fetch. Backend
  services should respond with actionable HTTP codes and body messages for easier troubleshooting.
- **Scalability** – the summary table defaults to 200 rows. Adjust this limit in `ReportSummaryDataService` if the catalogue
  requires larger extracts, and consider pagination or export actions for heavy datasets.

## Related assets
- Angular implementation: `app/erp/erp-reports/report-summary-view/*` and navbar search assets.
- Backend implementation: `io.github.erp.domain.ReportMetadata`, REST resource, services, and seeder extension.
- SQL reference: see `erp-system/queries/report-metadata-reference.sql` for a baseline inspection query.
