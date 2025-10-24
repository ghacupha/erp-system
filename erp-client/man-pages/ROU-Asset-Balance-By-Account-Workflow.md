# ROU Asset Balance by Account Report Workflow Update

## Overview
The ROU Asset Balance by Account summary view now calls the dedicated endpoint that requires a lease period identifier in the URL path. This change makes the client request structure match the server contract exposed by `RouAccountBalanceReportItemResourceProd#getRouAccountBalanceReportItems` and prevents any attempts to filter by lease liability when using this report.

## Navigation Path
1. Navigate to **ROU Reports ▸ ROU Asset Balance Report** in the ERP navigation drawer.
2. The application resolves the report metadata via `ReportMetadataService` and loads the summary view component at `app/erp/erp-reports/report-summary-view`.

## Updated Request Flow
1. The metadata seeder provides the backend path `api/leases/rou-account-balance-report-items/reports/{leasePeriodId}`.
2. `ReportSummaryDataService.fetchSummary` inspects the metadata path, replaces `{leasePeriodId}` with the selected period identifier, and issues a GET request to `/api/leases/rou-account-balance-report-items/reports/<leasePeriodId>`.
3. Only pagination parameters remain in the query string; the `leasePeriodId` is no longer appended as a criteria filter.
4. The server responds with the report rows for the specific lease period.

## Lease Period Search Behaviour
* The lease period selector now issues fresh lookups whenever a user types a new period code.
* Searches use the `periodCode.contains` filter on `LeasePeriodResource` so that partial entries like `202502` narrow the result set.
* When the user selects a lease period from the refreshed options, the component reloads the summary data using the selected identifier.

## Workflow Outcomes
* Eliminates accidental filtering by lease liability for this report.
* Exposes the lease period identifier explicitly in network traces, simplifying debugging.
* Ensures the selector can surface historical periods beyond the first 100 records returned at bootstrap.

## Related Source Files
* `src/main/webapp/app/erp/erp-reports/report-summary-view/report-summary-view.component.ts`
* `src/main/webapp/app/erp/erp-reports/report-summary-view/report-summary-data.service.ts`
* `src/main/java/io/github/erp/erp/reports/ReportMetadataSeederExtension.java`

