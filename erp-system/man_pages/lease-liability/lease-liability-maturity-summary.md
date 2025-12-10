# Lease Liability Maturity Summary

## Overview
The lease liability maturity summary groups outstanding lease principal and interest payable into three maturity
buckets relative to the end date of a selected lease repayment period. The dataset enables the treasury and
financial reporting teams to understand near-term versus long-term exposures captured in IFRS 16 schedules.

## Data flow
1. The client calls `GET /api/leases/lease-liability-schedule-report-items/liability-maturity-summary/{leasePeriodId}`.
2. `LeaseLiabilityScheduleReportItemResourceProd` delegates to the internal service layer, which executes the native
   aggregation in `InternalLeaseLiabilityScheduleReportItemRepository`.
3. The repository computes the days between each lease's contractual end date and the chosen repayment period end date.
4. Rows are bucketed into:
   * **≤365 days**
   * **366–1824 days**
   * **≥1825 days**
5. For each bucket the query sums the outstanding principal (`outstanding_balance`) and the closing interest payable
   (`interest_payable_closing`). Buckets with both sums equal to zero are excluded.
6. The internal mapper converts the projection to `LeaseLiabilityMaturitySummaryDTO`, ensuring the total column reflects
   the sum of principal and interest.

## Output columns
| Column | Description |
| --- | --- |
| `maturityLabel` | Readable label describing the maturity band. |
| `leasePrincipal` | Aggregated outstanding principal for the bucket. |
| `interestPayable` | Aggregated accrued interest payable for the bucket. |
| `total` | Convenience total of principal plus interest payable. |

## Usage notes
* Use the **Lease Period** filter in the reporting UI to target a specific repayment period.
* Negative day differences (expired leases) are treated as zero-day maturities and therefore fall into the **≤365 days** bucket.
* The SQL reference is available in `queries/lease-liability-maturity-summary.sql` for analysts who want to review or execute
  the aggregation manually.
