# Lease Posting Rule Category Sync (Technical Note)

## Why this update is needed
Lease posting rules can be created from legacy TA rule screens or from the unified posting rule configuration UI. When the debit or credit transaction accounts in the templates change, the posting rule header must also reflect the account categories of those accounts. This ensures the posting rule header and templates stay consistent, and it keeps downstream rule evaluation aligned with the ledger categories derived from the transaction accounts.

## Workflow summary
1. Collect TA rule data for every lease contract (interest accrual, interest paid transfer, repayment, and recognition).
2. Match the TA rules to lease posting rules using the `leaseContractId` posting rule condition and event type.
3. Update posting rule templates so debit/credit accounts mirror the TA rules.
4. Update the posting rule header debit/credit account category fields based on the selected transaction accounts.
5. Normalize the `leaseContractId` condition value so it matches the lease contract in the TA rule.

## Implementation notes
- The synchronization relies on the posting rule condition key `leaseContractId` with the `EQUALS` operator to associate a posting rule with a lease contract.
- Only rules with `module = 'LEASE'` are updated.
- Each posting rule is updated with the account categories derived from `transaction_account.account_category_id`.

## Operational script
Use the SQL script in `erp-system/queries/lease-posting-rule-category-sync.sql` to apply the update in PostgreSQL once TA rules or posting templates have been modified for existing lease contracts. The script repeats its CTEs for each `UPDATE`, so it can be executed as-is in tools that run each statement independently.
