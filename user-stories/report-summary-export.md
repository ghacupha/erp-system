# User Story: Export ROU Asset Balance Summary

## As a
Financial reporting analyst preparing offline audit packs for right-of-use assets.

## I want
* Dedicated CSV and Excel export buttons on the ROU Asset Balance summary view.
* The export to pull every record that matches the active filters, even when the on-screen table is paginated.

## So that
* I can reconcile balances in Excel without manually querying the API.
* I can share complete datasets with auditors and downstream reporting teams.

## Acceptance Criteria
1. **Visible controls** – After the report loads, I can see `Export CSV` and `Export Excel` buttons next to the report title. Buttons show a spinner while their respective export is in progress and remain disabled until the request completes.
2. **Full dataset** – Triggering either export calls the backend endpoint repeatedly until all pages are retrieved. The downloaded file contains every row returned by the API, not just the rows rendered on the first page.
3. **Consistent formatting** – Numbers and dates in the exported files match the formatting shown on screen (e.g., thousands separators, ISO dates).
4. **Error feedback** – If the backend returns no rows or an error occurs, an alert appears in the component header instead of starting a download.
5. **Timestamped filename** – Files download with timestamped names such as `rou-asset-balance-report-20251024-161500.xlsx` to simplify archival.

## Notes
* Export logic is shared through `report-summary-export.util.ts` so future reports can reuse the helper without duplicating formatting code.
* Unit tests cover the component export flow and the utility transformations to guard against regressions.
