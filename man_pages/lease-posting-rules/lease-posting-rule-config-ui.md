# Lease Posting Rule Configuration UI (Lease Module)

## Overview
The lease posting rule configuration UI consolidates the creation of transaction account posting rules, templates, and rule conditions into a single workflow. This is designed to support the new posting-rule evaluator pattern while preserving per-lease-contract granularity through rule conditions tied to the lease contract ID.

## Business Context
Lease postings frequently require different debit/credit accounts per contract, even for the same posting event. The UI enables accountants to create a lease-specific posting rule that contains:

- The overarching rule metadata (module, event type, account categories).
- One or more posting rule templates (debit/credit accounts, multiplier, line descriptions).
- One or more rule conditions (e.g., leaseContractId = 1001).

This makes it possible to define rules per contract and keep them in the unified posting-rule model.

## Workflow
1. The user opens the **Lease Posting Rule Configuration** screen.
2. The user selects the lease contract. The UI dispatches an NgRx action and loads lease-template suggestions.
3. Suggested debit/credit accounts (from the lease template) prefill the first posting-rule template and account categories.
4. The form includes a default rule condition with `leaseContractId` equal to the selected lease contract ID.
5. The user can add or remove templates and conditions, then save the rule.

## Key Components & Services
- **LeasePostingRuleConfigComponent**: UI and form state management for the multi-entity workflow.
- **TransactionAccountPostingRuleService**: Persists rules containing nested templates and conditions.
- **LeasePostingRuleConfigEffects**: Fetches lease template suggestions and pushes them into the store.

## Decisions & Rationale
- **NgRx draft synchronization**: The form dispatches draft updates to keep rule, template, and condition data in a single store payload.
- **Lease template suggestions**: Reuses lease template debit/credit accounts to reduce manual configuration errors.
- **Lease contract condition**: Defaults to `leaseContractId` so that posting rules remain per-contract without needing separate entities.

## Notes
- The UI intentionally limits module selection to `LEASE` to avoid cross-module errors.
- Posting rule templates and conditions are sent as nested payloads for persistence in one request.
