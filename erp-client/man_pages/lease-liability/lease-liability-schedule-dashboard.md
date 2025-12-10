# Lease Liability Schedule Dashboard

## Purpose
The lease liability schedule dashboard consolidates the headline IFRS16 balances for a single contract and exposes the detailed amortisation rows that make up the selected reporting period. It is accessible from the ERP leases area through the route `#/lease-liability-schedule-view/{contractId}` and requires the **ROLE_LEASE_MANAGER** authority.

## Data flows
- **Lease contract context** – fetched through `IFRS16LeaseContractService.find(contractId)` to display the lease title and identifiers.
- **Liability profile** – resolved by `LeaseLiabilityService.query({ 'leaseContractId.equals': contractId })` to read the initial liability amount and start date.
- **Reporting periods** – sourced from `LeaseRepaymentPeriodService.query` filtered by the contract and ordered by `sequenceNumber`. The component defaults to the last period returned and exposes the rest in a dropdown selector.
- **Schedule items** – collected through `LeaseLiabilityScheduleItemService.query` using the same contract filter and `sort=sequenceNumber,asc`. Each row is enriched with dayjs instances for the linked period’s start and end dates to support the formatting pipes.

## Aggregations
For the active reporting period the component filters the schedule array by `leasePeriod.id` and reduces the matching rows into the following totals:
- `cashTotal` – sum of `cashPayment` values.
- `principalTotal` – sum of `principalPayment` values.
- `interestTotal` – sum of `interestPayment` values.
- `outstandingTotal` – sum of the period end `outstandingBalance` values.
- `interestPayableTotal` – sum of `interestPayableClosing` amounts.

The headline cards display these totals alongside the liability’s start date, the selected period’s end date, and the original liability amount. The table view also surfaces a derived “days since previous payment” metric by computing the dayjs diff between consecutive period boundaries.

## Excel export structure
- The `exportDashboardToExcel` helper now assembles a 12-column matrix so three logical panels (Lease, Stats, Reporting) can be rendered across the top of the worksheet via merged ranges.
- Panel data sources:
  - **Lease** – lease title, contract identifier, and liability reference for quick context.
  - **Stats** – formatted totals for the initial liability and the cash, principal, and interest payments within the active period.
  - **Reporting** – the reporting period label, schedule start and close, plus the closing outstanding and interest payable balances.
- After the panels a merged "Monthly schedule" heading precedes the familiar 11-column amortisation table. The component reuses the date/number formatters so the export matches the dashboard presentation.

## Period filtering rules
- The dropdown lists every repayment period associated with the contract.
- Selecting a period resets the grid to only those schedule rows where `leasePeriod.id` matches the chosen identifier.
- The totals card section is recomputed for each selection using fresh reductions over the filtered rows.
- When no periods or rows exist the UI disables the selector and shows the empty state banner in the table body.

## Error handling
If any of the data calls fail the component surfaces an inline Bootstrap alert and keeps the existing state untouched. Loading feedback is provided through a centred spinner while the forkJoin request is in flight.
