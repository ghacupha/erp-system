# Lease liability transaction posting service

## Overview

Lease transaction posting previously relied on native SQL `INSERT ... SELECT` statements embedded in repositories such as:

- `LeaseRepaymentTransactionDetailsRepository`
- `LeaseInterestAccrualTransactionDetailsRepository`
- `LeaseInterestPaidTransferTransactionDetailsRepository`
- `LeaseLiabilityRecognitionTransactionDetailsRepository`
- `ROUAmortizationTransactionDetailsRepository`
- `LeaseRouRecognitionTransactionDetailsRepository`

Those insert statements joined schedule tables (`lease_liability_schedule_item`, `rou_depreciation_entry`) and rule tables (`talease_*_rule`, `taamortization_rule`) to resolve debit/credit accounts, generate descriptions, and populate `transaction_details` in bulk.

## Change summary

The posting flow is now driven by a dedicated service layer that builds `TransactionDetails` entities in Java, resolves posting rules from the rule repositories, and persists the results through the standard JPA repository. This decouples posting logic from hard-coded SQL joins and ensures rule resolution is performed via the rule entities.

Key behaviors maintained:

- Batch/tasklet entry points remain unchanged and still trigger posting in bulk.
- Schedule batch posting still runs against all schedule items with non-zero amounts, matching the existing SQL filters.
- Posting descriptions and dates follow the same string assembly rules as before.

## Service workflow

1. Tasklets invoke the transaction detail services as before.
2. Transaction detail services delegate to the new posting service.
3. The posting service loads the relevant schedule or source entities with fetch joins.
4. Rule repositories are queried up-front and mapped by lease contract.
5. For each eligible entry, a `TransactionDetails` instance is created with:
   - A new entry ID from `transaction_entry_id_sequence`.
   - Posting metadata (posted-by user, requisition ID, transaction type).
   - Amounts and accounts resolved from rule entities.
6. `InternalTransactionDetailsRepository.saveAll(...)` persists the batch in one call.

## Notes

- Rule lookups throw an error if a lease contract does not have a matching rule, preventing silent posting without configured accounts.
- Description formatting keeps the original SQL string concatenation behavior (e.g., fiscal month codes stripped of the `YM` prefix).
