# Story: Treasury analyst reviews lease liability maturity

## Persona
Treasury analyst responsible for liquidity planning.

## Goal
Identify how much lease liability principal and interest is due within one year versus longer horizons for a selected
lease repayment period.

## Steps
1. Navigate to **Reports → Lease Liability Maturity**.
2. Use the **Lease Period** dropdown to choose the target repayment period.
3. Run the report to retrieve the maturity buckets.
4. Review the table values for the ≤365 days, 366–1824 days, and ≥1825 days bands.

## Acceptance criteria
* The report only displays maturity bands where the combined principal and interest total is non-zero.
* Totals equal the sum of the principal and interest columns per row.
* The analyst can export or screenshot the data for board reporting without additional filtering.
