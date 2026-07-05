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

## Apply Template Defaults to Amortization Rules
1. Open the TA Amortization Rules screen and start a new rule.
2. Select an IFRS16 lease contract that already references a lease template.
3. The Debit field prefills with the template’s depreciation account and the Credit field with the accrued depreciation account when those mappings exist.
4. Adjust either account if needed, then save the rule.

## Apply Template Defaults to Lease Interest Accrual Rules
1. Open the Lease Interest Accrual Rule screen and start a new rule.
2. Choose an IFRS16 lease contract that is linked to a lease template.
3. When the template includes interest accrued accounts, the Debit and Credit fields auto-populate with those defaults.
4. Override either account as needed before saving the rule.

## Apply Template Defaults to Lease Recognition Rules
1. Open the TA Lease Recognition Rules screen and start a new rule.
2. Select an IFRS16 lease contract that references a lease template.
3. The form retrieves the full lease template (even when the contract record only references it) so the Debit field auto-fills with the template’s lease recognition debit account and the Credit field with the lease recognition credit account when defined.
4. Adjust either account if necessary, then save the rule.

## Apply Template Defaults to ROU Recognition and Lease Repayment Rules
1. Open the ROU Recognition or Lease Repayment rule screen and start a new rule.
2. Select an IFRS16 lease contract that references a lease template.
3. The form fetches the full lease template and pre-populates debit and credit accounts with the template mappings when available.
4. Confirm the defaults or override them before saving.
