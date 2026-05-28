# Unallocated Prepayment Accounts Report

## Story

As a finance reviewer, I want to search for a prepayment reconciliation report so that I can find prepayment accounts whose amortisation entries have not fully utilised the original prepayment amount.

## Flow

1. The reviewer opens the ERP application.
2. The reviewer types `unallocated prepayment` or `prepayment` into the search reports panel.
3. The reviewer selects `Unallocated Prepayment Accounts`.
4. The application opens a report table listing materially outstanding prepayment accounts.
5. The reviewer uses the outstanding amount, account numbers, dealer, and latest amortisation date to investigate incomplete utilisation.

## Expected Outcome

Only prepayment accounts with a remaining balance of at least `1.00` are shown. Fully utilised accounts and tiny rounding differences are excluded from the reconciliation list.
