# Lease template defaults for TA Lease Recognition Rules

## Context
TA lease recognition rules now react to the selected IFRS16 lease contract by pulling debit and credit defaults directly from the attached lease template. This reduces manual account entry and keeps new rules aligned with the template’s recognition mapping.

## Behaviour
1. User selects an IFRS16 lease contract while editing or creating a TA lease recognition rule.
2. The client fetches the full lease contract to access its `leaseTemplate`.
3. When the template defines `leaseRecognitionDebitAccount` and/or `leaseRecognitionCreditAccount`, the form:
   - Adds those accounts to the dropdown collections if they are missing.
   - Patches `debit` and `credit` with the template values, preserving any existing value when a template account is absent.
4. If the lease has no template or the template omits both accounts, the listener leaves the form unchanged.

## Notes
- The listener includes null checks to avoid patching when no account defaults exist.
- The behaviour is covered by Angular unit tests for both the standard and ERP-branded TA lease recognition rule forms.
