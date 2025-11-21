# ROU depreciation schedule view

## Overview

The depreciation run for right-of-use assets now exposes a schedule view that mirrors the lease liability dashboard. Analysts can select an IFRS16 lease contract and review each lease period's amortisation, from the initial ROU amount through to the fully depreciated balance.

## Backend changes

- A production endpoint at `GET /api/leases/rou-depreciation-schedule-view/{leaseContractId}` returns period-ordered rows with the initial amount, depreciation charge and outstanding net book value derived from `RouDepreciationEntry` and `LeasePeriod`.
- Report metadata seeding registers the page under `erp/rou-depreciation-schedule-view`, allowing the navbar report search to surface the new view alongside other lease reports.

## Front-end flow

- The new Angular component `RouDepreciationScheduleViewComponent` (path: `/erp/rou-depreciation-schedule-view/:leaseContractId?`) loads available IFRS16 lease contracts and drives the schedule API for the active selection.
- Users can switch contracts via a dropdown, see headline totals (initial ROU, cumulative depreciation and closing NBV) and review the detailed per-period rows.

This alignment keeps ROU depreciation insight discoverable from the reports search while providing a focused dashboard for auditors and controllers.
