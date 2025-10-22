# Dynamic Report Metadata Platform – Client Experience Guide

## Purpose
This guide documents how the Angular client renders dynamic report summaries driven by server-side metadata. It is intended for
front-end contributors onboarding to the reporting platform and for QA engineers verifying navigation workflows.

## Key components
- **ReportSummaryViewModule** – lazily loaded route at `/reports/view/:slug` that hosts the generic summary component.
- **ReportSummaryViewComponent** – orchestrates metadata lookup, optional filter loading, summary retrieval, and table rendering.
- **ReportSummaryDataService** – resolves backend API URLs (relative or absolute) and appends query parameters derived from the
  metadata configuration.
- **ReportMetadataService** – exposes `search`, `findByPagePath`, and `listActive` helpers to keep the client in sync with the
  catalogue.
- **Navbar search** – integrates report discovery in the global navigation bar with debounced lookup and keyboard support.

## User journey
1. **Search** – as the user types at least two characters in the navbar search box, the client calls
   `ReportMetadataService.search(term, 8)` and surfaces a dropdown containing titles, descriptions, and router slugs.
2. **Navigate** – selecting a result collapses the navbar and routes to `/reports/view/<slug>`, which lazy loads the summary
   module.
3. **Metadata resolution** – the component fetches metadata via `findByPagePath('reports/view/<slug>')`. Missing metadata shows a
   descriptive error and invites the user to retry via search.
4. **Filter hydration** – depending on the metadata flags:
   - `displayLeasePeriod` triggers a call to `LeasePeriodService.query({ size: 100, sort: ['id,desc'] })`. The most recent period
     becomes the default selection.
   - `displayLeaseContract` triggers a call to `LeaseLiabilityService.query({ size: 100, sort: ['id,desc'] })` with a similar
     defaulting strategy.
   Loading states are tracked independently so users understand which selector is still fetching.
5. **Summary display** – once prerequisites are ready, `ReportSummaryDataService.fetchSummary(backendApi, params)` loads the
   data, a responsive table is rendered, and any previously displayed errors are cleared.

## Interaction details
- **Column derivation** – the component inspects the first record to establish column order, then appends additional keys found in
  subsequent records so heterogeneous payloads remain readable.
- **Value formatting** – numbers use locale-aware formatting, dayjs instances convert to `YYYY-MM-DD`, and objects fall back to
  JSON stringification for debugging insight.
- **Error messaging** – a single `errorMessage` surface consolidates metadata, filter, and summary failures. Loading states avoid
  overwriting errors until asynchronous operations conclude.
- **Accessibility** – the search dropdown supports keyboard submission via Enter, and screen-reader text is provided on the clear
  button.

## Extending the UI
1. Update `ReportMetadata` to include new feature toggles or query parameters.
2. Inject corresponding services into `ReportSummaryViewComponent` and follow the pattern in `prepareFilters` to manage loading
   states and defaults.
3. Adjust the summary template/SCSS as necessary to accommodate new filter controls.
4. Enhance navbar behaviour by extending `ReportMetadataService` if additional search filters (module, tags) are required.

## QA checklist
- Verify search results only display `active=true` reports and handle API failures gracefully.
- Confirm navigation preserves or closes the dropdown and collapses the navbar on mobile breakpoints.
- Validate that the summary view warns when metadata lacks a backend API or when the API returns an empty dataset.
- Check that filter changes re-trigger summary loading and that query parameters match the metadata configuration.

Refer to the server operations guide for entity management and the SQL appendix for backend validation queries.
