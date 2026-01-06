# TA Amortization Rule Template Defaults

## Overview
When a user selects an IFRS16 lease contract on the TA amortization rule form, the UI now reuses the lease template’s depreciation accounts to populate debit and credit. This keeps amortization postings consistent with lease template configuration.

## UI Flow
- Component: `ta-amortization-rule-update` in the ERP Accounts area.
- Interaction: Change the **Lease Contract** lookup.
- System behaviour:
  - The component retrieves the selected contract with `IFRS16LeaseContractService.find`.
  - If the contract has a template:
    - `debit` is patched with `leaseTemplate.depreciationAccount` (when provided) **only if the debit field is currently empty**, so existing mappings remain untouched on load.
    - `credit` is patched with `leaseTemplate.accruedDepreciationAccount` (when provided) **only if the credit field is empty**, preventing accidental overwrites for existing rules.
  - If no template exists, no patching occurs so manual selections remain intact.

## Testing
- Added a unit test to confirm debit and credit fields are patched after choosing a lease that carries a template.
