# Lease Posting Rule Configuration UI (Client)

## Purpose
This client-side workflow allows a lease accountant to create a posting rule, its templates, and rule conditions in a single form. The UI relies on NgRx to keep the draft state synchronized while the user configures contract-specific accounts.

## Data Flow
1. Lease contract selection triggers a store action.
2. NgRx effects load the lease template and derive suggested debit/credit accounts.
3. The component patches the first template and account-category fields using those suggestions while suppressing value-change events to avoid feedback loops.
4. The save action submits a single payload with:
   - Rule metadata (module, event type, account categories).
   - Rule templates (debit/credit accounts, multipliers).
   - Rule conditions (leaseContractId rule).

## Expected Outcome
The API receives a complete posting-rule payload with nested templates and conditions, enabling the posting-rule evaluator to resolve contract-specific accounts for lease transactions.

## Performance Considerations
Suggestion selectors only emit when suggestion fields change, preventing UI hangs when users edit other form fields.
