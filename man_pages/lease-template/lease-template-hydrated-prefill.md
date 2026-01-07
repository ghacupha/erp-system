# Lease template hydration for TA rule defaults

## Overview
Transaction-account (TA) rule forms such as amortization, lease recognition, ROU recognition, lease repayment, and lease interest accrual rely on lease templates to prefill debit/credit accounts. The lease contract record returned from the UI selection does not always contain fully hydrated lease template relations, so the TA forms must explicitly fetch the lease template by ID to ensure that account mappings are available for prefill.

## Business purpose
Finance users expect TA rule forms to auto-populate accounts immediately after selecting an IFRS16 lease contract. Ensuring the lease template is hydrated avoids missed defaults and reduces manual entry errors when the lease contract response contains only a template stub.

## Workflow changes
1. User selects an IFRS16 lease contract in a TA rule update form.
2. The form resolves the contract by fetching it from the lease contract service.
3. If the contract includes a lease template reference, the form fetches the full lease template instance by ID.
4. Debit/credit defaults are patched into the form and the transaction account collections are refreshed to include those accounts.

## Components and services involved
- TA rule update components in the ERP accounts module observe `leaseContract` value changes.
- `IFRS16LeaseContractService.find` retrieves the lease contract.
- `LeaseTemplateService.find` hydrates the lease template using the referenced template ID.
- The TA rule forms patch debit/credit values once the template is fully loaded.

## Rationale
Earlier implementations assumed that the lease contract response always contained full lease template data. In practice, the response can include a template stub (ID only). Explicitly fetching the template by ID ensures reliable prefill behavior and aligns with how other dependent entities are hydrated in the ERP client.
