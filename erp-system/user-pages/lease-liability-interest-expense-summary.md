# How to run the Lease Liability Interest Expense Summary

1. Open the report screen and pick the desired lease period from the drop-down.
2. Click **Generate**. The client calls the backend endpoint `.../interest-expense-summary/{leasePeriodId}`.
3. Verify the returned rows. Each row contains the lease number, dealer, narration, debit account, credit account, interest
   expense, cumulative annual total and the cumulative value up to the previous month.
4. Use the narration and account numbers directly in your posting template. No manual totals are required—the service returns
   fully aggregated values.
