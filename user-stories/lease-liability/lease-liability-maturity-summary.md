# Story: Treasury analyst reviews lease liability maturity

**Persona:** Treasury analyst planning liquidity buffers.

**Need:** Understand short-, medium-, and long-term lease obligations for a given repayment period.

**Scenario:**
1. Open the **Lease Liability Maturity** report.
2. Select the lease repayment period from the dropdown filter.
3. Run the report.
4. Review the resulting maturity bands (≤365, 366–1824, ≥1825 days) and note the totals where present.

**Acceptance Criteria:**
- Only non-zero maturity bands appear in the table.
- The `total` column equals `leasePrincipal + interestPayable` for each row.
- Data reflects the repayment period end date supplied in the filter.
