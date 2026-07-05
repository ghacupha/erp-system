# Lease posting rule configuration

## Overview
The Lease Posting Rule Configuration area lets you review, create, and update lease posting rules that control ledger postings for lease events.

## Accessing the list
1. Open **Accounts → Lease Posting Rule Config**.
2. The list displays all lease posting rules with search, sorting, and paging controls.

## Searching and reviewing rules
- Use the search bar to filter rules by name, identifier, module, or event type.
- Click **View** to open the full rule configuration, including templates and conditions.

## Creating a rule
1. Select **Create a new Lease Posting Rule**.
2. Complete the rule header details and add templates and conditions.
3. Save to return to the list.

## Editing a rule
1. From the list, click **Edit** on the rule you want to change.
2. Update the pre-filled fields, templates, or conditions.
3. Save to return to the list.

## Syncing account categories after template changes
When you update a lease posting rule template’s debit or credit account for an existing lease contract, the posting rule header categories must be re-aligned to match the account categories of the selected accounts. Run the lease posting rule category sync SQL script to update the posting rule header categories, templates, and `leaseContractId` conditions after the update is saved.

## Deleting a rule
1. From the list, click **Delete** next to the rule.
2. Confirm the deletion in the dialog.
