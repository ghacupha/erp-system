# TA Amortization Rule Template Defaults

## Overview
TA amortization rules now pull debit and credit defaults from the selected IFRS16 lease contract’s template. This keeps depreciation postings aligned with the template’s configured accounts when setting up amortization rules for lease contracts.

## Workflow Details
- Location: `ta-amortization-rule-update` component under ERP Accounts.
- Trigger: Changing the **Lease Contract** control.
- Behaviour:
  - The component fetches the selected IFRS16 lease contract via `IFRS16LeaseContractService.find`.
  - If the contract has a lease template:
    - `debit` is patched with `leaseTemplate.depreciationAccount` when present.
    - `credit` is patched with `leaseTemplate.accruedDepreciationAccount` when present.
  - If no template is linked, the form is left untouched to preserve manual entries.

## Design Notes
- A `valueChanges` subscription on `leaseContract` performs the lookup, using null checks to avoid unnecessary calls.
- Patching only occurs when template accounts are defined, preventing accidental overwrites when users need manual selections.
- The behaviour is covered by a unit test that verifies debit and credit values update after selecting a lease that carries a template.
