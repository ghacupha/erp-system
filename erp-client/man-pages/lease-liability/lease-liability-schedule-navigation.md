# Lease Liability Schedule Navigation (Client Perspective)

* The NgRx store now exposes `ifrs16LeaseContractReportState` for the selected
  lease contract id.
* `Ifrs16LeaseReportNavComponent` provides the `/lease-liability-schedule-report/report-nav`
  workflow using `<jhi-m21-ifrs16-lease-form-control>` and dispatches the
  selection before routing to the dashboard.
* `ReportSummaryViewComponent` watches the store when the slug equals
  `lease-liability-schedule-report`, applies the stored id to the dynamic filter
  set and redirects back to the parameter form when the id is missing.
* Destroying either the nav component or the dashboard dispatches
  `ifrs16LeaseContractReportReset` so the selection is cleared between sessions.
* Local automation is pending because `npm install` cannot download the Cypress
  binary (HTTP 403), so the unit specs should be executed once the dependency
  installation succeeds in a full environment.
