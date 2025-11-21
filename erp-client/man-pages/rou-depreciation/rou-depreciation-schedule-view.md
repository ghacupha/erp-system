# ROU depreciation schedule dashboard (client)

## Purpose

Finance users can now open a dedicated dashboard for the right-of-use depreciation schedule and move between lease contracts without leaving the view. The page mirrors the lease liability dashboard but uses lease periods to follow the ROU amortisation lifecycle.

## Routing and discovery

- Route: `/erp/rou-depreciation-schedule-view/:leaseContractId?` guarded by `ROLE_LEASE_MANAGER`.
- Report search entry: seeded with page path `erp/rou-depreciation-schedule-view`, so typing "ROU depreciation schedule" in the navbar suggestions navigates straight to the component.

## UI behaviour

- The dropdown at the top lists IFRS16 lease contracts (booking ID and title). Choosing a contract triggers a navigation that reloads the schedule for the new contract id.
- Totals at the top show the initial ROU amount, cumulative depreciation and closing net book value derived from the loaded schedule.
- The table lists each lease period with start/end dates, the amortisation charge and the remaining net book value. Periods are shown in the order returned by the API to reflect the depreciation run.

## Data access

The view calls `GET /api/leases/rou-depreciation-schedule-view/{leaseContractId}` through `RouDepreciationScheduleViewService`, which converts date strings to `dayjs` objects for display.
