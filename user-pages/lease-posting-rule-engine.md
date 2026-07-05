# Lease Posting Rule Engine

## Purpose
Lease postings now use the posting rule engine. Posting rules define which debit and credit accounts are used for a specific lease event (repayment, interest accrual, liability recognition, and so on), along with optional conditions and amount multipliers.

Legacy TA rule screens (repayment, interest accrual, interest paid transfer, lease recognition, ROU recognition, and amortization) are replaced by the posting rule engine for production posting.

## Configure a Posting Rule
1. Open the posting rule administration page.
   - For lease workflows, use the **Lease Posting Rule Config** screen to create the rule, templates, and conditions together.
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

## Template Suggestions by Event Type
When you select a lease contract in the **Lease Posting Rule Config** screen, the form checks the lease template and suggests debit/credit accounts for the chosen **Event Type**. If the lease template does not define accounts for that event, the form clears any existing suggestions so you can choose the correct accounts manually.

Use the event type to determine which template fields are used:
- `LEASE_LIABILITY_RECOGNITION` → lease recognition debit/credit accounts.
- `LEASE_REPAYMENT` → lease repayment debit/credit accounts.
- `LEASE_INTEREST_ACCRUAL` → interest accrued debit/credit accounts.
- `LEASE_INTEREST_PAID_TRANSFER` → interest paid transfer debit/credit accounts.
- `LEASE_ROU_RECOGNITION` → ROU recognition debit/credit accounts.
- `LEASE_ROU_AMORTIZATION` → depreciation and accrued depreciation accounts.

## Replace Legacy Rules
Use posting rules with the following event types to replace legacy rule screens:
- `LEASE_REPAYMENT`
- `LEASE_INTEREST_ACCRUAL`
- `LEASE_INTEREST_PAID_TRANSFER`
- `LEASE_LIABILITY_RECOGNITION`
- `LEASE_ROU_RECOGNITION`
- `LEASE_ROU_AMORTIZATION`

Create one posting rule per event and mirror the debit/credit accounts from the legacy configurations in the posting templates.

## Migrate Existing Configurations
1. Export or note the debit/credit accounts from the legacy rule screens.
2. Create the matching posting rules and templates in the posting rule administration page.
3. Add conditions (such as `leaseContractId`) when rules are lease-specific.
4. Run a posting batch to confirm the generated transaction details match expectations.

## Run Lease Posting
When you post lease events, the system:
1. Builds a posting context with the lease event data.
2. Finds the matching posting rule for the module/event.
3. Applies rule conditions and emits balanced transaction details from the templates.

If no rule matches or a rule has no templates, the posting run will report a configuration error so the rule can be corrected.

## Avoid Overlapping Rules
The posting engine expects exactly one rule to match a given module/event context. If multiple rules match, the posting run will stop with an error that lists the conflicting rules. Review rule conditions and deactivate or refine overlaps before retrying the posting.
