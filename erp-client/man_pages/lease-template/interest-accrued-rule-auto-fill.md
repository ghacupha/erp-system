## Interest accrued rule lease template auto-fill

### Context and purpose

TA Lease Interest Accrual Rule creation now listens for changes to the selected lease contract. When the lease has a linked template with interest accrued mappings, the form defaults the debit and credit accounts. This mirrors the existing defaults for other lease rule types and reduces manual re-entry of template-defined accounts.

### Behaviour

* On lease selection, the UI retrieves the full lease (including its template) via `IFRS16LeaseContractService.find`.
* If the lease template provides `interestAccruedDebitAccount` or `interestAccruedCreditAccount`, the form patches the Debit and Credit controls with those values.
* When editing an existing rule, the handler keeps any saved Debit and Credit selections that differ from the template defaults; template accounts are only applied when the controls are empty or already match those defaults.
* The transaction account options are refreshed with any template-provided accounts so the dropdown still includes template choices when saved rules use different mappings.
* If no template exists or neither interest accrued account is defined, the handler leaves existing form values unchanged.
* Null and ID checks guard the subscription to avoid unnecessary fetches and accidental overwrites when defaults are not present.

### Notes and rationale

The change centralises the template-driven defaults for accrued interest rules while remaining override-friendly. Users can still select different debit or credit accounts after the defaults are applied. Defensive guards prevent patching when the lease or template payload is incomplete and ensure the form retains user-entered values if a template lacks the required mappings.
