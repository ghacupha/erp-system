# Lease Liability Schedule Navigation Update

## Overview

The IFRS16 lease liability schedule dashboard is now fronted by a lightweight
parameter screen. Lease managers select an IFRS16 lease contract before they
arrive at the dynamic dashboard (`reports/view/lease-liability-schedule-report`).
The selection is persisted in the NgRx store so that the dashboard loads with the
chosen contract applied to its filter panel.

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
2. Submitting the form dispatches the selection action and navigates to the
   dashboard route.
3. `ReportSummaryViewComponent` watches the store when the slug is
   `lease-liability-schedule-report`. If no contract id is available the user is
   redirected back to the parameter screen. Otherwise the stored id is applied
   to the dashboard filters and the summary data is refreshed.
4. Leaving the dashboard triggers the reset action so subsequent visits start
   with a clean state.

## Report Metadata

* The `ReportMetadataSeederExtension` seeds a definition for
  `reports/view/lease-liability-schedule-report` that exposes the backend API
  `api/leases/lease-liability-schedule-items` with two filters:
  `leaseContractId.equals` (typeahead) and `leasePeriodId.equals` (dropdown).
  This keeps the dynamic dashboard reachable after deployments and aligns the
  auto-applied contract selection with the available query parameters.

## Testing

* Unit tests cover the reducer, selectors and the navigation component.
* `report-summary-view.component.spec.ts` exercises the guard logic to ensure
  missing selections trigger a redirect and that stored selections prime the
  dashboard filters.
* Automated execution remains pending in this workspace because `npm install`
  fails while downloading the Cypress binary (HTTP 403 via the build proxy).
