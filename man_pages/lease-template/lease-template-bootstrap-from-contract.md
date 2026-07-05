# Lease template bootstrapping from IFRS16 lease contracts

## Overview
Lease templates can be created directly from an existing IFRS16 lease contract. The list and detail views for IFRS16 lease contracts expose a **Create Template** action that bootstraps a new template by hydrating data from the selected contract and all related transaction-account rules. The resulting lease template opens in edit mode already prefilled, so users only need to provide a template title and adjust fields that require customization.

## Business purpose
Lease renewals often reuse the same service outlet, dealer, and transaction-account mappings from the expiring contract. Bootstrapping templates from the contract reduces data entry and ensures the template mirrors existing account structures, enabling rapid setup for the next lease contract in the series (for example, creating template "Lease 1023" from contract "Lease 1001").

## Workflow
1. A user clicks **Create Template** from the IFRS16 lease contract list or detail view.
2. An NgRx effect dispatches a bootstrapping workflow that loads the selected contract and queries transaction-account rule entities filtered by the same lease-contract relationship.
3. The effect uses `forkJoin` to call the required services in parallel, then assembles a fully hydrated lease template payload.
4. The UI navigates to the lease template update form with the prefilled data already patched in.
5. The user provides the template title and adjusts any fields that require customization, then saves.

## Components and services involved
- IFRS16 lease contract list and detail components trigger the **Create Template** action.
- The lease template NgRx effects orchestrate the bootstrapping flow.
- Lease contract services fetch the selected contract.
- Transaction-account rule services query by the lease-contract relationship and supply account mappings.
- The lease template update form receives the prefilled model for final edits and save.

## Rationale and considerations
- Prefilling templates ensures the same transaction-account rules are preserved across renewals while still giving users the option to change values for the new contract.
- By centralizing the data gathering in NgRx effects with `forkJoin`, the UI avoids partial state updates and opens the editor with a consistent, fully hydrated template.
- Users can immediately proceed to create IFRS16 lease contracts and related schedules for the new contract number using the templated defaults.
