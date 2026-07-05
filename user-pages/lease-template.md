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

## Create a Template from an IFRS16 Lease Contract
1. Open the IFRS16 lease contract list or detail view.
2. Select **Create Template** for the lease contract you want to renew.
3. Review the prefilled lease template update form populated with contract details and related transaction-account rules.
4. Enter a template title (for example, "Lease 1023") and adjust any fields that need customization.
5. Save the template.

This option is designed for renewals, so you can reuse the same dealers, service outlet, and transaction-account mappings from the expiring contract while only updating the template title or other fields that need change.

## Use a Template on a Lease Contract
1. Open an IFRS16 lease contract and select Edit.
2. Choose a lease template from the Lease Template dropdown.
3. Confirm the service outlet and main dealer fields are populated as expected.
4. Save the contract.

## Apply Template Defaults to Posting Rules
Posting rules now replace the legacy TA rule screens. Use lease templates as a reference when selecting debit and credit accounts in posting templates.

1. Open the posting rule administration page and start a new rule for module `LEASE`.
2. Choose the event type (for example, `LEASE_REPAYMENT`, `LEASE_INTEREST_ACCRUAL`, `LEASE_INTEREST_PAID_TRANSFER`, `LEASE_LIABILITY_RECOGNITION`, `LEASE_ROU_RECOGNITION`, or `LEASE_ROU_AMORTIZATION`).
3. Add a posting template and select the debit and credit accounts. Use the lease template's account mappings to pick the correct ledgers.
4. Add optional conditions (such as `leaseContractId`) when rules should only apply to specific leases.
5. Save the posting rule.
