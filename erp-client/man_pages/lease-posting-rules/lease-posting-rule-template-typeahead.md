# Lease Posting Rule Template Typeahead and Account Category Prefill

## Overview
The Lease Posting Rule Configuration page now uses a typeahead-based transaction account selector for debit and credit template entries. This matches the TA amortization rule pattern and allows filtering accounts as users type while keeping both fields on the same row.

## Workflow changes
- Event type and lease contract selections continue to trigger NgRx effects that fetch the lease template and suggest debit/credit accounts.
- The template debit and credit fields use the `jhi-m21-transaction-account-form-control` component so account lookups are loaded dynamically as users type.
- When a user selects a transaction account, an NgRx effect derives and pre-fills the corresponding account category fields from the selected account relationship.

## Implementation notes
- Account prefill occurs only when the template fields are empty to preserve user overrides.
- Account category updates do not overwrite suggested debit/credit accounts in state.
