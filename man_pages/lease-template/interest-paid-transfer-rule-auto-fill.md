## Interest paid transfer rule lease template auto-fill

### Context and purpose

The `TAInterestPaidTransferRule` update form now listens for changes to the selected lease contract and fetches the lease (including its template) to default the debit and credit accounts. This reduces manual entry when a lease template already defines `interestPaidTransferDebitAccount` and `interestPaidTransferCreditAccount`.

### Behaviour

* On lease selection, the UI retrieves the lease with its template and, when available, patches the debit and credit form controls with the template’s transfer accounts.
* Account options are refreshed to include any template-provided accounts so the selections remain valid in the dropdowns.
* If the selected lease has no template (or lacks transfer accounts), the handler leaves existing form values untouched.
* Users can override the prefilled accounts at any time by selecting different debit and credit accounts before saving.

### Notes and rationale

The value-change handler is guarded to avoid null templates and to prevent overwriting manually chosen values when no template defaults exist. Prefilling only occurs after an explicit lease selection, ensuring existing records load unchanged while still allowing a user-driven override.
