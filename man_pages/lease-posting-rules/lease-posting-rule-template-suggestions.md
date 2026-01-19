# Lease Posting Rule Template Suggestions by Event Type

## Business Context
Lease posting rules inherit default debit and credit accounts from a lease template to reduce manual configuration and keep postings consistent. When the lease contract changes, the UI should reflect whether the linked lease template has matching defaults for the currently selected event type. If it does not, the UI must explicitly clear any previous suggestions so stale accounts are not saved unintentionally.

## Workflow Overview
1. The user selects a lease contract in the lease posting rule configuration screen.
2. The client loads the lease contract to identify the linked lease template.
3. The client loads the lease template and maps debit/credit suggestions based on the selected event type:
   - Lease liability recognition uses lease recognition accounts.
   - Lease repayment uses lease repayment accounts.
   - Interest accrual uses interest accrued accounts.
   - Interest paid transfer uses interest paid transfer accounts.
   - ROU recognition uses ROU recognition accounts.
   - ROU amortization uses depreciation and accrued depreciation accounts.
4. If the template is missing accounts for that event type, the suggestions resolve to `null`, clearing the corresponding form controls.

## Design Decisions
- **Event-type mapping** keeps the defaults aligned with the posting rule event. This avoids applying lease-recognition defaults to other events.
- **Explicit clearing** of debit/credit and account type fields prevents accidental submission of stale values when a contract has no template or missing fields.

## Notes for Testing
- Verify that switching between event types updates the suggested accounts without changing the selected lease contract.
- Verify that selecting a contract without template defaults clears debit/credit and account type fields.
