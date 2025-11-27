# ROU depreciation schedule view

## Overview

The depreciation run for right-of-use assets now exposes a schedule view that mirrors the lease liability dashboard. Analysts can search for an IFRS16 lease contract through a navigation form and review each lease period's amortisation, from the initial ROU amount through to the fully depreciated balance.

## Backend changes

- A production endpoint at `GET /api/leases/rou-depreciation-schedule-view/{leaseContractId}` returns period-ordered rows with the initial amount, depreciation charge and outstanding net book value derived from `RouDepreciationEntry` and `LeasePeriod`.
- The endpoint accepts an optional `asAtDate` (`YYYY-MM-DD`) query parameter so the client can request the schedule up to a specific cut-off.
- Report metadata seeding registers the page under `erp/rou-depreciation-schedule-view`, allowing the navbar report search to surface the new view alongside other lease reports.
- A companion navigation page at `/rou-depreciation-schedule-view/report-nav` uses the M21 IFRS16 lease control to locate the contract and forwards the user to the dashboard with the selected id.
- The schedule data now chains each row's initial amount from the previous period's outstanding balance so the table mirrors the running net book value shown in the source depreciation entries.

## Front-end flow

- The new Angular component `RouDepreciationScheduleViewComponent` (path: `/erp/rou-depreciation-schedule-view/:leaseContractId?`) loads available IFRS16 lease contracts and drives the schedule API for the active selection.
- A navigation component `RouDepreciationScheduleNavComponent` validates a contract choice, dispatches the selection to the store and routes to `/erp/rou-depreciation-schedule-view/{id}`.
- Users can switch contracts via a dropdown, adjust an **As at** date picker (defaulting to today) and see headline totals (initial ROU, cumulative depreciation and closing NBV) recalculated to that cut-off while still reviewing the full per-period schedule.
- CSV and Excel export buttons mirror the report-summary export workflow but are scoped to the depreciation table; they generate a full extract of the displayed schedule without any pagination limits.

This alignment keeps ROU depreciation insight discoverable from the reports search while providing a focused dashboard for auditors and controllers.

### November 2025 regression fix

Some environments send the depreciation schedule with snake-case column aliases (for example `period_start_date` and `depreciation_amount`). The Angular service now normalises those responses into camelCase before rendering, ensuring the headline totals and table cells populate even when the API returns tuple-backed maps.
