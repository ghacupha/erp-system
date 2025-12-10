# ROU depreciation schedule contract search

## Context
The ROU depreciation schedule view now uses the reusable IFRS16 lease contract search control instead of a plain dropdown. The prior implementation loaded up to fifty contracts on page init, which was slow on large portfolios and could miss leases beyond that cap.

## Change summary
- Integrated the `jhi-m21-ifrs16-lease-form-control` search field to drive schedule selection.
- The component now fetches the selected contract on demand via the lease contract service, preserving deep links to `/rou-depreciation-schedule-view/:id`.
- Contract searches rely on the server-side lease search endpoint rather than a fixed client-side list; selecting a result triggers navigation and reloads the schedule.

## Notes and rationale
- The search control retains the pre-selected lease when the route already contains an ID, ensuring bookmarked URLs still populate the header control.
- Error handling for invalid IDs remains unchanged and continues to surface inline alerts.
