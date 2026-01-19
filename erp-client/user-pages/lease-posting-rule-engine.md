# Lease Posting Rule Engine

## Purpose
Lease postings use the posting rule engine. Posting rules define which debit and credit accounts are used for a lease event (repayment, interest accrual, liability recognition, and so on), along with optional conditions and amount multipliers.

## Configure a Posting Rule
1. Open the posting rule administration page.
   - For lease workflows, use the **Lease Posting Rule Config** screen to create the rule, templates, and conditions together.
2. Create a new rule and set:
   - **Module**: `LEASE`.
   - **Event Type**: choose the lease event (e.g., `LEASE_REPAYMENT`, `LEASE_INTEREST_ACCRUAL`).
3. Add rule conditions when the rule should apply only in a specific scenario (for example, `leaseContractId = 42`).
4. Add one or more posting templates:
   - Select the debit account.
   - Select the credit account.
   - Optionally add an amount multiplier if the line should scale the posting amount.
   - Optionally supply a line description to override the posting description.
5. Save the rule.

## Template Suggestions by Event Type
When you select a lease contract in the **Lease Posting Rule Config** screen, the form checks the lease template and suggests debit/credit accounts for the chosen **Event Type**. If the lease template does not define accounts for that event, the form clears any existing suggestions so you can choose the correct accounts manually.

Use the event type to determine which template fields are used:
- `LEASE_LIABILITY_RECOGNITION` → lease recognition debit/credit accounts.
- `LEASE_REPAYMENT` → lease repayment debit/credit accounts.
- `LEASE_INTEREST_ACCRUAL` → interest accrued debit/credit accounts.
- `LEASE_INTEREST_PAID_TRANSFER` → interest paid transfer debit/credit accounts.
- `LEASE_ROU_RECOGNITION` → ROU recognition debit/credit accounts.
- `LEASE_ROU_AMORTIZATION` → depreciation and accrued depreciation accounts.
