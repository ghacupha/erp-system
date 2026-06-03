# Unallocated Prepayment Amortizations Report

## Purpose

The report identifies prepayment amortisation entries that have not been linked to a specific prepayment account. This usually occurs when amortisation rows are generated or imported without a parent account reference, leading to mismatches between the total amortised amounts and the recognised prepayment balances.

## Identification

The report selects all active `prepayment_amortization` rows where `prepayment_account_id` is null. These entries represent costs that have been "amortised" but are not tracking against a specific asset (prepayment account).

## Integration

The report is registered as `Unallocated Prepayment Amortizations` in report metadata. It is discoverable via the search reports panel.

The backend endpoint is `GET /api/prepayments/unallocated-prepayment-amortizations`. It returns detailed information about the orphan amortisation entries, including the description, period, amount, accounts, and the compilation request that produced them.

The matching review SQL is available at `erp-system/queries/unallocated-prepayment-amortizations.sql`.
