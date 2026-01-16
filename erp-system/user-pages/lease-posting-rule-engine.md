# Lease Posting Rule Engine

## Purpose
Lease postings now use the posting rule engine. Posting rules define which debit and credit accounts are used for a specific lease event (repayment, interest accrual, liability recognition, and so on), along with optional conditions and amount multipliers.

## Configure a Posting Rule
1. Open the posting rule administration page.
2. Create a new rule and set:
   - **Module**: `LEASE`.
   - **Event Type**: choose the lease event (e.g., `LEASE_REPAYMENT`, `LEASE_INTEREST_ACCRUAL`).
   - Optional scenario fields such as variance type or invoice timing.
3. Add rule conditions when the rule should apply only in a specific scenario (for example, `leaseContractId = 42`).
4. Add one or more posting templates:
   - Select the debit account.
   - Select the credit account.
   - Optionally add an amount multiplier if the line should scale the posting amount.
   - Optionally supply a line description to override the posting description.
5. Save the rule.

## Run Lease Posting
When you post lease events, the system:
1. Builds a posting context with the lease event data.
2. Finds the matching posting rule for the module/event.
3. Applies rule conditions and emits balanced transaction details from the templates.

If no rule matches or a rule has no templates, the posting run will report a configuration error so the rule can be corrected.

## Avoid Overlapping Rules
The posting engine expects exactly one rule to match a given module/event context. If multiple rules match, the posting run will stop with an error that lists the conflicting rules. Review rule conditions and deactivate or refine overlaps before retrying the posting.
