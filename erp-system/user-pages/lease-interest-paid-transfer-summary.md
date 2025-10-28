# How To: Retrieve the Lease Interest Paid Transfer Summary

1. Open the reporting console and navigate to **Lease Liability Reports**.
2. Choose the repayment period that you want to reconcile.
3. Issue a GET request to
   `/api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}`
   (the console supplies the period identifier automatically).
4. Review the returned JSON array. Each item shows the lease booking ID, dealer
   name, narration, debit account number, credit account number, and the total
   interest paid for that lease within the selected period.
5. Use the debit and credit account numbers to cross-check ledger postings in the
   general ledger module.

The endpoint suppresses leases whose cash interest payment is zero, helping you
focus on the transactions that need reconciliation.
