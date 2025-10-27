# Lease Liability Interest Expense Summary Report

## Business context

The finance team needs an auditable listing of interest expense postings for IFRS 16 lease liabilities. The listing must
consolidate the monthly interest accrued per lease and provide cumulative values for the current financial year so that the
entries can be pushed to the core banking system. The report is parameterised by a **lease period identifier**. That identifier
corresponds to the lease repayment period whose closing month drives both the periodic and cumulative amounts that appear in
the listing.

## Data sourcing

* **Primary fact table** – `lease_liability_schedule_item`. The report aggregates the `interest_accrued` column.
* **Lease context** – joins to `lease_liability` (for the ERP lease number) and `ifrs16lease_contract` (for the booking ID,
  lease title and dealer information).
* **Posting accounts** – derived from `talease_interest_accrual_rule`, joining the debit and credit `transaction_account`
  records configured for each lease contract.
* **Period filtering** – the lease period identifier is resolved through `lease_repayment_period`. The fiscal year is
  derived from the linked `fiscal_month`. Where a fiscal month is not assigned, the `period_code` (formatted as `YYYYMM`)
  is used to determine the calendar year and the cumulative window.

The native SQL query that powers the report is stored in
[`queries/lease-liability-interest-expense-summary.sql`](../../erp-system/queries/lease-liability-interest-expense-summary.sql).
It uses CTEs to first isolate the target lease period, then accumulate the year-to-date interest before combining everything
with the transactional context.

## API surface

A production resource endpoint exposes the data at:

```
GET /api/leases/lease-liability-schedule-report-items/interest-expense-summary/{leasePeriodId}
```

The controller delegates to `InternalLeaseLiabilityScheduleReportItemService`, which executes the native query via the
`InternalLeaseLiabilityScheduleReportItemRepository`. Mapping from the projection to the DTO is centralised in
`LeaseLiabilityInterestExpenseSummaryInternalMapper` to keep the REST layer free from transformation logic.

## Output shape

Each row in the response payload contains:

* `leaseNumber` – ERP lease identifier.
* `dealerName` – main dealer associated with the contract.
* `narration` – concatenation of the booking ID and lease title to mirror the posting description.
* `creditAccount` / `debitAccount` – transaction account numbers supplied by the TA lease interest accrual rule.
* `interestExpense` – current-period interest accrued for the supplied lease period.
* `cumulativeAnnual` – January-to-current cumulative interest in the same fiscal year as the lease period.
* `cumulativeLastMonth` – cumulative interest up to the month preceding the supplied lease period (zero for January).

## Posting workflow

1. The accountant selects the relevant period in the UI, which forwards the `leasePeriodId` to the backend.
2. The API returns the aggregated dataset described above.
3. Finance reviews the debit/credit accounts and the cumulative totals before generating the core banking journal.
4. Because the dataset includes both current month and cumulative balances, the team can quickly validate the evolution
   of interest expense across the year without running multiple reports.
