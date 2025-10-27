# Story: Produce the lease liability interest expense summary

## Persona
Finance accountant

## Context
- I select the target lease period in the reporting UI.
- The request is sent to `/api/leases/lease-liability-schedule-report-items/interest-expense-summary/{leasePeriodId}`.

## Acceptance criteria
1. The result set contains one row per lease that has schedule items in the specified period.
2. Each row displays:
   - Lease number (from `lease_liability.lease_id`).
   - Narration = booking ID + lease title.
   - Dealer name (main dealer on the contract).
   - Debit and credit account numbers from the TA Lease Interest Accrual Rule.
   - `interestExpense`, `cumulativeAnnual`, `cumulativeLastMonth` derived from `interest_accrued` using the fiscal-year window.
3. The API performs all numeric aggregation in the database and returns ready-to-post values—no further calculations are
   required on the client.
