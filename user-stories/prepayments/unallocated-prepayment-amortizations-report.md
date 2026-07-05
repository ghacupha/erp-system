# Unallocated Prepayment Amortizations Report

## Story

As a finance reviewer, I want to identify prepayment amortisation entries that are not allocated to any prepayment account so that I can correct the source data and ensure that all amortisations are correctly mapped to their respective prepayment assets.

## Flow

1. The reviewer opens the ERP application.
2. The reviewer types `unallocated prepayment` or `prepayment` into the search reports panel.
3. The reviewer selects `Unallocated Prepayment Amortizations`.
4. The application opens a report table listing all active amortisation entries that lack a parent prepayment account.
5. The reviewer uses the description, period, amount, and compilation request ID to trace the origin of the orphan entries and re-allocate them as needed.

## Expected Outcome

All active prepayment amortisation entries without a `prepayment_account_id` are displayed in the report, allowing for easy identification of data integrity issues.
