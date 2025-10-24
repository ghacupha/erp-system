# User Story: Refresh ROU Asset Balance Report by Lease Period

## As a
Financial analyst investigating right-of-use asset balances for a specific reporting period.

## I want
* The ROU Asset Balance report to present a lease-period focused selector that searches by period code.
* The report request to surface the lease period identifier explicitly in the API path.

## So that
* I can validate period-specific asset balances without cross-filtering by lease liability.
* I can debug report outputs by cross-referencing the lease period identifier in API traces and logs.

## Acceptance Criteria
1. **Typeahead search** – When I type `202502` in the lease period selector, the dropdown reloads with matching lease periods even if they are not in the initial page of results.
2. **Path parameter** – The network call that loads the summary data uses the URL format `/api/leases/rou-account-balance-report-items/reports/<LEASE_PERIOD_ID>`.
3. **Filter scope** – The report UI does not render a lease liability selector for this summary view.
4. **Data refresh** – Selecting a different lease period triggers a fresh API request and updates the report table.

## Notes
* The metadata definition for the report must specify `leasePeriodId` as the placeholder used in the backend path template.
* Placeholder substitution is handled in `ReportSummaryDataService`, ensuring only pagination values remain in the query string.

