# User Story: Consume Dynamic Report Metadata on the Client

## Narrative
As a front-end engineer I want the Angular application to translate metadata into interactive report summaries so that business
users can browse new analytics immediately after metadata is published.

## Acceptance criteria
1. **Search widget** – The navbar exposes a compact search field that:
   - Debounces keystrokes and hits `ReportMetadataService.search` after two characters.
   - Displays a dropdown with report titles, descriptions, and paths.
   - Supports keyboard submission using the Enter key.
2. **Routing** – Selecting a search result navigates to `/reports/view/<slug>` and triggers lazy loading of
   `ReportSummaryViewModule` without a full reload.
3. **Metadata awareness** – `ReportSummaryViewComponent` resolves metadata by path and updates the document title with the report
   name.
4. **Filter controls** – Based on metadata flags, the component loads lease periods and/or lease liabilities, surfaces selectors
   with loading spinners, and defaults to the newest option when available.
5. **Summary rendering** – Once filters are ready, the component calls `ReportSummaryDataService.fetchSummary`, renders a table
   with auto-generated columns, and gracefully handles empty responses and API errors.

## Workflow trace
1. Navbar emits search terms to `reportSearch$`, which orchestrates the `ReportMetadataService` calls and updates the dropdown
   state.
2. Router navigation passes the slug to `ReportSummaryViewComponent` via route params.
3. `prepareFilters` fetches optional datasets; `handleFilterRequestCompletion` ensures the summary loads only after all selectors
   finish initialising.
4. `loadSummaryData` constructs query parameters using metadata hints and selected entities, then updates the UI with results or
   error states.
