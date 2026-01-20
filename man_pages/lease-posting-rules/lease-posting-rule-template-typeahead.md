# Lease Posting Rule Template Typeahead and Account Category Prefill

## Overview
The Lease Posting Rule Configuration UI now uses the same transaction-account typeahead control that exists on TA amortization rules, ensuring the debit and credit account fields remain searchable without preloading the full account list. This change keeps the debit/credit controls on the same row while allowing users to refine results by typing a few characters.

## Workflow changes
- When the user selects an event type and lease contract, the NgRx workflow still looks up the lease template and pre-fills the first posting-rule template with suggested debit and credit accounts.
- The debit and credit template fields are now backed by the `jhi-m21-transaction-account-form-control` component. The component streams search terms into the transaction-account suggestion service so that results are fetched on demand.
- When a user manually selects a debit or credit account from the typeahead, the form dispatches a template-account selection action. An NgRx effect maps the selected transaction account to its account category and updates the debit/credit account category fields without overwriting any existing account selections.

## Implementation notes
- Prefill of the debit/credit accounts is applied only when those template fields are still empty. This prevents user-overrides from being reset by subsequent account-category updates.
- The reducer preserves existing suggested debit/credit accounts when only account-category updates are dispatched, keeping the NgRx workflow stable for both initial suggestions and manual overrides.

## Related components
- Lease Posting Rule Configuration component
- Transaction account form control (typeahead)
- Lease posting rule NgRx effects and reducer
