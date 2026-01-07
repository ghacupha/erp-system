# Lease template hydration for TA rule defaults

## Persona
Lease accountant configuring transaction-account (TA) rules for IFRS16 lease contracts.

## Scenario
The accountant needs TA rule forms to auto-populate debit and credit accounts when they select an IFRS16 lease contract linked to a lease template.

## Steps
1. Open a TA rule update form (amortization, lease recognition, ROU recognition, lease repayment, or lease interest accrual).
2. Select an IFRS16 lease contract that is linked to a lease template.
3. Wait for the form to load the related template accounts.
4. Review the prefilled debit and credit accounts.
5. Save the rule or adjust the accounts if needed.

## Expected outcome
The form fetches the full lease template and pre-populates the debit and credit accounts based on the template mappings, even when the lease contract record initially contains only a template reference.
