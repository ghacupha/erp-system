# Lease Liability Maturity Summary Report

The Lease Liability Maturity report highlights how much principal and accrued interest is due within short-, medium-, and
long-term windows.

## Accessing the report
1. From the main navigation, choose **Reports**.
2. Select **Lease Liability Maturity**.

## Filtering
* **Lease Period** – choose the repayment period whose end date should be used to calculate days to maturity. This filter
  is required before running the report.

## Reading the results
The grid displays a row for each non-empty maturity bucket:

| Column | Description |
| --- | --- |
| Maturity Label | One of ≤365 days, 366–1824 days, or ≥1825 days. |
| Lease Principal | Sum of outstanding principal balances for leases within the bucket. |
| Interest Payable | Sum of accrued interest payable balances. |
| Total | Automatically calculated as principal plus interest for the bucket. |

Buckets where both principal and interest total zero are automatically hidden. Expired leases appear in the ≤365 days
bucket.
