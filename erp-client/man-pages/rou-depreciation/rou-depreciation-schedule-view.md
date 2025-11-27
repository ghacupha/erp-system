# ROU depreciation schedule dashboard (client)

## Purpose

Finance users can now open a dedicated dashboard for the right-of-use depreciation schedule and move between lease contracts without leaving the view. A navigation form mirrors the lease liability schedule launcher: it uses the IFRS16 lease contract search control so the user can find the correct contract, dispatches the selection to the store, and forwards them to the depreciation dashboard with the contract id in the route. The page then mirrors the lease liability dashboard but uses lease periods to follow the ROU amortisation lifecycle.

## Routing and discovery

- Navigation form: `/rou-depreciation-schedule-view/report-nav` (guarded by `ROLE_LEASE_MANAGER`) hosts the searchable M21 IFRS16 lease contract control and opens the dashboard for the selected contract.
- Route: `/erp/rou-depreciation-schedule-view/:leaseContractId?` guarded by `ROLE_LEASE_MANAGER`.
- Report search entry: seeded with page path `erp/rou-depreciation-schedule-view`, so typing "ROU depreciation schedule" in the navbar suggestions navigates straight to the component.

## UI behaviour

- The navigation form validates that a lease contract has been selected, dispatches the `ifrs16LeaseContractReportSelected` action, and routes to `/erp/rou-depreciation-schedule-view/{id}`.
- The dropdown at the top of the dashboard lists IFRS16 lease contracts (booking ID and title). Choosing a contract triggers a navigation that reloads the schedule for the new contract id, so the table can still be switched without returning to the launcher.
- Totals at the top show the initial ROU amount, cumulative depreciation and closing net book value derived from the loaded schedule.
- The table lists each lease period with start/end dates, the amortisation charge and the remaining net book value. The "Initial amount" column uses the previous period's outstanding balance after the first row so the running NBV chain is visible directly in the grid. Periods are shown in the order returned by the API to reflect the depreciation run.
- Export buttons generate CSV or Excel downloads of the visible table using the shared report-summary export helpers.

## Data access

The view calls `GET /api/leases/rou-depreciation-schedule-view/{leaseContractId}` through `RouDepreciationScheduleViewService`, which converts date strings to `dayjs` objects for display.

### Recent fix: snake-case responses

Some gateways return the schedule rows with snake-case keys (for example, `period_start_date` and `depreciation_amount`). The client now normalises both camelCase and snake-case payloads before rendering so that dates, charges and balances always appear in the summary cards and table.
