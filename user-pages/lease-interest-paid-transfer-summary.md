# Lease Interest Paid Transfer Summary

Use this guide when you need to verify which accounts captured the cash interest
settlement for a lease repayment period.

1. Navigate to the Lease Liability reports in the ERP console.
2. Select the repayment period you want to audit.
3. Trigger the summary endpoint
   `/api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}`.
4. Review the response. Only leases with a non-zero cash interest payment are
   listed. Each row shows the lease booking ID, dealer name, narration and the
   debit/credit account numbers that the transfer rule uses.
5. Compare the debit and credit accounts with the ledger postings to confirm the
   settlement is complete.
