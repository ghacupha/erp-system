# Lease posting rule list and detail

## Persona
Lease accountant responsible for maintaining lease posting rules.

## Story
As a lease accountant, I want a list of lease posting rules with search and CRUD actions so that I can review existing rules, inspect their details, and update them without recreating them from scratch.

## Steps
1. Navigate to **Accounts → Lease Posting Rule Config**.
2. Use the search bar to filter rules by name, identifier, module, or event type.
3. Select **View** to open a rule in the full configuration form.
4. Click **Edit** from the detail view to update the rule.
5. Save changes and return to the list to confirm the update.

## Expected outcome
- The list shows all current rules with search, sorting, and paging.
- The detail view displays templates and conditions exactly as they were configured.
- Updates persist and return the user to the list.
