# Lease Liability Interest Expense Summary Report

1. Navigate to **Reports → Leases → Lease Liability Interest Expense Summary**.
2. Choose the reporting period by selecting the required lease period identifier. The list mirrors the codes displayed in the
   Lease Periods module (e.g. `202505`).
3. Submit the request. The system calls the backend API to retrieve the interest expense summary for the selected month.
4. Review the output table:
   - **Lease Number** – ERP lease identifier.
   - **Dealer** – main dealer on the lease contract.
   - **Narration** – booking ID plus lease title; copy this into the journal description.
   - **Debit Account / Credit Account** – posting accounts derived from the `LEASE_INTEREST_ACCRUAL` posting rule template.
   - **Interest Expense** – current month interest accrued.
   - **Cumulative (Annual)** – year-to-date interest (January to selected month).
   - **Cumulative (Up to last month)** – cumulative interest excluding the selected month (zero in January).
5. Export or copy the listing into the posting template for the core banking system.

> Tip: because the cumulative totals are included, you can confirm month-on-month movements without running prior periods
> separately. Simply compare the "Cumulative (Annual)" and "Cumulative (Up to last month)" columns.
