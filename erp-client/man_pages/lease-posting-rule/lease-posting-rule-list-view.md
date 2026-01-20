# Lease posting rule list and detail workflow

## Overview
Lease posting rules previously relied on the single configuration form and returned users to their prior navigation state after saving. That meant there was no dedicated place to review existing rules or revisit them for updates. This change introduces a list-first workflow so that all lease posting rules can be searched, reviewed, and managed with standard CRUD actions.

## Business purpose
Lease posting rules control how lease events translate into ledger postings. Finance users need a consolidated view to validate rule coverage, inspect the conditions/templates that drive postings, and update rules in place without recreating them.

## Workflow summary
1. Users open the *Lease Posting Rules* list, which provides search, sorting, paging, and actions for view, edit, and delete.
2. Selecting **View** opens the rule in the full configuration form (including templates and conditions) with fields locked to prevent accidental edits.
3. Selecting **Edit** opens the same full configuration form, now populated with the existing rule data and ready for updates.
4. Saving returns the user to the list view to confirm the change and continue working.

## Key components and decisions
- The list view uses the existing `TransactionAccountPostingRuleService` to query and search rules, keeping backend APIs unchanged.
- The configuration form now accepts route modes (`new`, `view`, `edit`) to control whether fields are editable.
- Posting rule templates and conditions are rebuilt from the stored data so that the detail view matches the original create experience.
- Lease contract selection is inferred from the `leaseContractId` condition, preserving the relationship the rules rely on.

## Rationale
A list-and-detail pattern matches other ERP screens and ensures rule management is discoverable. The same form is used for create, view, and edit to avoid divergent layouts and to keep the rule context consistent across workflows.
