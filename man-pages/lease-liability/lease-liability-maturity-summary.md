# Lease Liability Maturity Summary

This report aggregates outstanding lease principal and accrued interest payable into three maturity buckets using the
selected lease repayment period as the valuation date. The API endpoint `GET
/api/leases/lease-liability-schedule-report-items/liability-maturity-summary/{leasePeriodId}` executes the native query
outlined in `queries/lease-liability-maturity-summary.sql`.

## Buckets
* **≤365 days** – contracts maturing within the next financial year (including already expired agreements, which are
  treated as zero days to maturity).
* **366–1824 days** – medium-term obligations covering the next two to five years.
* **≥1825 days** – long-term exposure beyond five years.

Each bucket returns the summed principal (`leasePrincipal`), interest payable (`interestPayable`), and a `total` column
that represents the combined amount. Buckets with zero totals are omitted from the payload to keep downstream reports clean.
