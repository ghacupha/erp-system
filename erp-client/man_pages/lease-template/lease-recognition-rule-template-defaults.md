# Lease template defaults for TA Lease Recognition Rules

## Context
TA lease recognition rules now respond to the selected IFRS16 lease contract by pre-filling debit and credit accounts from the linked lease template. This keeps recognition postings consistent across contracts and reduces manual entry.

## Behaviour
1. When a lease contract is chosen in the TA lease recognition rule form, the client loads that contract to access its `leaseTemplate`.
2. If the template defines `leaseRecognitionDebitAccount` and/or `leaseRecognitionCreditAccount`, the form:
   - Adds those accounts to the available selections if they are not already loaded.
   - Patches the `debit` and `credit` controls with the template values, leaving existing entries untouched when a template account is missing.
3. When no template defaults exist, the form remains unchanged.

## Testing
- Angular unit tests verify that selecting a lease with template defaults patches the debit and credit fields and that the transaction account collections include the newly loaded accounts.
