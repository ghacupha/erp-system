# Lease Templates

## Overview
Lease templates store shared account mappings and reference data that stay consistent across renewed IFRS16 lease contracts. Use them to reduce repetitive data entry and keep contract setup consistent.

## Create a Lease Template
1. Navigate to the Lease Templates list.
2. Select "Create a new Lease Template".
3. Enter a required template title.
4. Choose the transaction accounts, asset category, service outlet, and main dealer that should apply to related leases.
5. Save the template.

## Copy a Lease Template
1. From the Lease Templates list or detail view, select "Copy".
2. Adjust any fields as needed.
3. Save to create a new template.

## Use a Template on a Lease Contract
1. Open an IFRS16 lease contract and select Edit.
2. Choose a lease template from the Lease Template search field (type to filter options).
3. Confirm the service outlet and main dealer fields are populated as expected.
4. Save the contract.

## Apply Template Defaults to Amortization Rules
1. Navigate to TA Amortization Rules and start a new rule.
2. Choose an IFRS16 lease contract that is linked to a lease template.
3. Debit and Credit auto-fill with the template’s depreciation and accrued depreciation accounts when available **only if those fields are empty**, so opening an existing rule will not overwrite saved accounts.
4. Keep or override those defaults, then save the rule. To reapply template defaults on an existing rule, clear the debit and credit values before reselecting the lease contract.
