# Lease Liability Maturity Summary Report

## Business context

Treasury and statutory reporting teams need a single snapshot that shows how undiscounted lease cash flows roll off after the
selected repayment period. The maturity summary answers that question by grouping cash payments into three IFRS 16 monitoring
buckets—**Current Period**, **Next 12 Months**, and **Beyond 12 Months**—for every active contract. The dataset lets accountants
prepare liquidity tables without extracting the full schedule.

## Data sourcing

* **Fact source** – `lease_liability_schedule_item` supplies the cash payment for each future repayment period.
* **Calendar anchor** – `lease_repayment_period` provides the selected period (`:leasePeriodId`) and the forward-looking
  sequence used to assign buckets.
* **Contract context** – `ifrs16lease_contract` and `dealer` enrich each row with the booking identifier and dealer name.
* **Query definition** – see [`queries/lease-liability-maturity-summary.sql`](../../queries/lease-liability-maturity-summary.sql)
  for the exact SQL executed by the reporting service. The script projects the three buckets and totals in a single pass.

## API surface

```
GET /api/leases/lease-liability-schedule-report-items/maturity-summary/{leasePeriodId}
```

The production controller resolves the lease period identifier, executes the maturity SQL, and returns one row per lease with
pre-computed bucket values. Consumers do not need to repeat the aggregation client side.

## Bucket logic

The SQL assigns bucket labels by comparing each schedule item's `sequence_number` to the anchor period:

* **Current Period** – `sequence_number` equals the anchor sequence; captures the cash due in the selected month.
* **Next 12 Months** – `sequence_number` is greater than the anchor and less than or equal to `anchor + 12`.
* **Beyond 12 Months** – `sequence_number` exceeds `anchor + 12`, representing longer-dated lease payments.

The query adds all cash payments across the buckets to produce `totalUndiscounted`, ensuring the sum reconciles back to the
undiscounted liability figure exported to statutory templates.

## Response schema

Each row in the response contains the following fields:

| Field | Description |
| --- | --- |
| `leaseId` | Booking identifier sourced from `ifrs16lease_contract`. |
| `dealerName` | Main dealer tied to the contract, for operational grouping. |
| `currentPeriod` | Cash payment due in the selected lease period. |
| `nextTwelveMonths` | Sum of payments scheduled for the following 12 periods. |
| `beyondTwelveMonths` | Sum of payments that fall beyond the one-year horizon. |
| `totalUndiscounted` | Total of the three maturity buckets. |

## Report catalogue placement

The UI entry is seeded as **Lease Liability Maturity** with page path `reports/view/lease-liability-maturity` in
[`ReportMetadataSeederExtension`](../../src/main/java/io/github/erp/erp/reports/ReportMetadataSeederExtension.java). Link your
man-page and user documentation to this metadata key so users can navigate to the feature directly.
