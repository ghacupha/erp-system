# Lease Template User Stories

## Story 1: Create a lease template
- Persona: Lease manager preparing reusable lease configurations.
- Steps: Open Lease Templates list, select "Create a new Lease Template", enter template title and account mappings, choose asset category, service outlet, and main dealer, then save.
- Expected outcome: A lease template is saved and appears in the Lease Templates list.

## Story 2: Copy a lease template
- Persona: Lease manager duplicating an existing template for a similar lease class.
- Steps: Open Lease Templates list or detail view, select "Copy", adjust fields if needed, and save.
- Expected outcome: A new lease template is created with the copied values.

## Story 3: Apply a lease template to a lease contract
- Persona: Lease manager renewing a contract and reusing shared settings.
- Steps: Open the IFRS16 lease contract update form, select a lease template, verify the service outlet and main dealer values, and save the contract.
- Expected outcome: The contract saves with shared fields populated from the template.

## Story 4: Prefill amortization rule accounts from a lease template
- Persona: Accountant configuring amortization rules for a lease contract.
- Steps: Open the TA amortization rule update form, choose an IFRS16 lease contract that has a lease template, confirm debit and credit prefill with the template’s depreciation and accrued depreciation accounts, then save or adjust as needed.
- Expected outcome: Debit and credit fields default to the template’s depreciation accounts, reducing manual entry while still allowing overrides.

## Story 5: Create a lease template from an IFRS16 lease contract
- Persona: Lease manager standardizing contract data into a reusable template.
- Steps: Open the IFRS16 lease contract list or detail view, select "Create Template", review the prefilled template fields sourced from the contract and related rules, update the template title if needed, and save.
- Expected outcome: A new lease template is created with accounts, asset category, service outlet, and main dealer populated from the selected lease contract context.
