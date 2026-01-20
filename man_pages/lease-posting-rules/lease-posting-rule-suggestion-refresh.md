# Lease posting rule suggestion refresh

## Business goal
Lease posting rule configuration should always propose debit and credit accounts that match both the selected event type and the chosen lease contract. Account suggestions must refresh when either input changes so accountants do not need to manually clear stale selections.

## Current workflow
1. The user selects a lease contract and event type in the Lease Posting Rule Configuration page.
2. The UI dispatches a request for lease-template suggestions scoped to the selected lease contract.
3. The suggestion response maps the event type to the lease template’s debit and credit transaction accounts.
4. The form applies the suggested accounts and account categories to the first posting rule template.

## Update: keep suggestions in sync with user selections
The UI now tracks the last suggested debit and credit accounts and account categories. When suggestions update, the form only replaces the current values if they are empty or still match the last suggested values. This ensures that:
- Switching event types or lease contracts refreshes the prefilled accounts when the existing values are still auto-filled.
- Manual overrides remain intact, because the form will not overwrite values that differ from the last suggestion.

## Design notes
- Suggestions are still derived from the lease template, using the event type to choose the correct debit and credit accounts.
- The guard on prefill updates prevents stale values from persisting when the selected lease contract changes.
- Manual selection stays prioritized, so the user is not surprised by data loss after making intentional edits.

## Related UI surface
Lease Posting Rule Configuration → Posting Rule Templates (debit and credit accounts).
