# Lease Liability Schedule Navigation Update

## Overview

The IFRS16 lease liability schedule dashboard is now fronted by a lightweight
parameter screen. Lease managers select an IFRS16 lease contract before they
arrive at the bespoke dashboard route (`/lease-liability-schedule-view/{id}`).
The selection is persisted in the NgRx store so that follow-up workflows can
reuse the chosen contract while the dedicated dashboard renders the full
amortisation view.

## Store Changes

* New actions `ifrs16LeaseContractReportSelected` and
  `ifrs16LeaseContractReportReset` maintain the selected lease contract id.
* The reducer slice `ifrs16LeaseContractReportState` stores the selected
  contract and exposes selectors through
  `selectedLeaseContractIdForReport`.
* `ErpStoreModule` registers the feature state under the key
  `ifrs16LeaseContractReport`.

## Navigation Workflow

1. The navigation component at `/lease-liability-schedule-report/report-nav`
   renders a single IFRS16 lease contract selector built on
   `<jhi-m21-ifrs16-lease-form-control>`.
2. Submitting the form dispatches the selection action and navigates directly to
   `/lease-liability-schedule-view/{id}` where the Angular feature module
   renders the contract-specific dashboard.
3. `ReportSummaryViewComponent` still guards the legacy slug; if someone lands
   on `/reports/view/lease-liability-schedule-report` the component immediately
   redirects back to the parameter screen so bookmarks remain valid.
4. Leaving the dashboard triggers the reset action so subsequent visits start
   with a clean state.

## Report Metadata

* The `ReportMetadataSeederExtension` now seeds the navigation entry at
  `lease-liability-schedule-report/report-nav` with a single
  `leaseContractId.equals` filter. The backend API remains
  `api/leases/lease-liability-schedule-items`, giving the search console enough
  information to surface the launch point while the bespoke dashboard handles
  period logic client-side.

## Testing

* Unit tests cover the reducer, selectors and the navigation component.
* `report-summary-view.component.spec.ts` exercises the guard logic to ensure
  missing selections trigger a redirect and that stored selections prime the
  dashboard filters.
* Automated execution remains pending in this workspace because `npm install`
  fails while downloading the Cypress binary (HTTP 403 via the build proxy).
