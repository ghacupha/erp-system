## Lease repayment rule lease template defaults

### Context and purpose

TA Lease Repayment Rule creation now listens for changes to the selected IFRS 16 lease contract. When that lease references a template with repayment mappings, the UI automatically defaults the debit and credit accounts so accountants do not have to re-enter template data.

### Behaviour

* The form subscribes to `leaseContract` value changes and fetches the selected lease through `IFRS16LeaseContractService.find` to ensure the template is loaded.
* If the template defines `leaseRepaymentDebitAccount` or `leaseRepaymentCreditAccount`, the handler patches the Debit and Credit controls with those accounts.
* Template-provided accounts are injected into the transaction account collection so the patched values remain selectable in the dropdowns.
* When no template is present, or the repayment accounts are missing, the handler leaves the user’s current Debit and Credit selections untouched.

### Notes and rationale

This aligns the repayment rule form with other lease rule screens that already consume template defaults. It reduces setup time, preserves manual overrides, and guards against unintended overwrites by exiting early when templates or repayment account references are absent.
