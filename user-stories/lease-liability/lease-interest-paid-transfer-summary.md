# User Story: Review Lease Interest Paid Transfer Summary

**Persona:** Lease Accountant responsible for validating monthly interest
settlements.

**Trigger:** Accountant selects a repayment period in the reporting console and
requests the interest paid transfer summary.

**Flow:**
1. The console invokes `GET /api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}`.
2. The service returns only leases whose aggregated cash interest payment is
   greater than zero.
3. Each row provides the lease booking ID, dealer name, narration, debit account
   number, credit account number, and the summed interest payment.

**Outcome:** Accountant reconciles the transfer postings without scanning through
leases that had zero cash interest activity.
