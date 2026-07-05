# User Story: Review Lease Interest Paid Transfer Summary

## Persona
*Lease Accountant* – responsible for reconciling monthly lease liability cash
settlements with the configured transfer rules.

## Goal
Review the cash interest payments settled in a specific repayment period,
confirm that only leases with non-zero transfers are listed, and capture the
debit/credit accounts used for posting.

## Steps
1. Open the reporting console and navigate to the lease liability reports.
2. Choose the repayment period of interest.
3. Trigger the API call `GET /api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}`
   via the reporting client.
4. Review the returned rows, each containing the lease booking ID, dealer name,
   narration, debit account, credit account, and total interest paid.

## Acceptance Criteria
- Only leases with a positive interest payment appear in the response.
- Both debit and credit account numbers are visible for every row.
- The totals match the native query documented in
  `queries/lease-interest-paid-transfer-summary.sql`.
