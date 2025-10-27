# Lease Liability Interest Expense Summary Report

## Business context

The finance team requires a consolidated posting file for IFRS 16 lease liabilities. The listing must show the current-period
interest expense alongside cumulative values for the financial year so that journal entries can be posted in the core banking
platform. The backend therefore exposes a report driven by a single **lease period identifier** representing the closing month
of interest.

## Data sourcing

* **Fact rows** – `lease_liability_schedule_item` where `interest_accrued` is aggregated per lease.
* **Lease metadata** – `lease_liability` (ERP lease ID) and `ifrs16lease_contract` (booking ID, lease title, dealer).
* **Posting accounts** – `talease_interest_accrual_rule` joins to debit and credit `transaction_account` entries configured for
each lease contract.
* **Period resolution** – `lease_repayment_period` joins to `fiscal_month`; when a fiscal month is absent, the numeric
  `period_code` (format `YYYYMM`) is used to determine the in-year cumulative window.

The exact SQL lives in [`queries/lease-liability-interest-expense-summary.sql`](../../queries/lease-liability-interest-expense-summary.sql)
and is executed as a native query in the repository. It constructs CTEs to isolate the target period, compute cumulative
interest up to that period and finally enrich the results with account data.

## API surface

```
GET /api/leases/lease-liability-schedule-report-items/interest-expense-summary/{leasePeriodId}
```

`LeaseLiabilityScheduleReportItemResourceProd` invokes `InternalLeaseLiabilityScheduleReportItemService`, which coordinates the
repository and the projection mapper (`LeaseLiabilityInterestExpenseSummaryInternalMapper`) so that the REST layer returns a
clean DTO collection.

## Payload columns

* `leaseNumber`
* `dealerName`
* `narration` (booking ID + lease title)
* `creditAccount`
* `debitAccount`
* `interestExpense`
* `cumulativeAnnual`
* `cumulativeLastMonth`

These values mirror the spreadsheet shared by finance, allowing the same file to be imported into the posting workflow without
manual recomputation.
