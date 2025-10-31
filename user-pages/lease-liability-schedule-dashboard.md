# Lease Liability Schedule Dashboard – User Manual

The schedule dashboard is a targeted workspace for Lease Managers who need to review IFRS16 liability activity without exporting spreadsheets.

## Launching the page
1. Confirm that your role includes `ROLE_LEASE_MANAGER`.
2. From the leases module navigate to the URL `#/lease-liability-schedule-view/{contractId}`.
3. The latest repayment period loads automatically so the top cards reflect the most current balances.

## Reading the dashboard
- **Context header** – confirms the lease title and liability reference.
- **Period selector** – lists all repayment periods tied to the contract. Selecting another entry immediately recalculates totals and refreshes the table.
- **Headline metrics** – show the initial liability, schedule start date, selected period close date, and aggregate cash, principal, interest, outstanding, and interest-payable figures for the active period.
- **Monthly schedule** – presents each amortisation row with formatted values and the number of days since the previous payment, helping you validate timing differences.

## Tips
- Use the selector to compare historical periods; the view always keeps the most recent period available for quick return.
- If a period has no data you will see an empty-state message in the table. Review your amortisation compilation before retrying.
- Error banners appear if the underlying services are unavailable—retry once the connection stabilises.
