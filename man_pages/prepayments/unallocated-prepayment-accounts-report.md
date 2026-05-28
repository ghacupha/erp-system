# Unallocated Prepayment Accounts Report

## Purpose

The report identifies prepayment accounts whose active amortisation entries have not consumed the recognised prepayment amount. It supports reconciliation work where the ledger or schedules show that a prepayment still has a material balance after amortisation processing.

## Calculation

For each `prepayment_account`, the report sums related `prepayment_amortization` rows where `inactive` is not true. The outstanding amount is:

```text
prepayment_account.prepayment_amount - sum(active prepayment_amortization.prepayment_amount)
```

Rows are included only when the outstanding amount is at least `1.00`, so tiny rounding residue is ignored by default. The endpoint accepts `minimumOutstandingAmount` for future tuning.

## Integration

The report is registered as `Unallocated Prepayment Accounts` in report metadata. The search reports panel can therefore discover it without adding a dedicated front-end route.

The backend endpoint is `GET /api/prepayments/unallocated-prepayment-accounts`. It returns paginated rows with the account identity, dealer, debit and transfer accounts, currency, recognised amount, amortised amount, outstanding amount, active amortisation count, and latest amortisation period.

The matching review SQL is available at `erp-system/queries/unallocated-prepayment-accounts.sql`.
