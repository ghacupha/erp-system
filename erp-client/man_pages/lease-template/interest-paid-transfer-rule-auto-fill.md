## Interest paid transfer rule lease template auto-fill (client)

### Context and purpose

The client-side `TAInterestPaidTransferRule` update component now reacts to lease selection changes by loading the chosen lease (with its template) and defaulting the debit and credit accounts from the template’s `interestPaidTransferDebitAccount` and `interestPaidTransferCreditAccount`.

### Behaviour

* When a lease is selected, the component requests the lease with its template and patches the debit and credit controls with the template’s transfer accounts when present.
* Template accounts are merged into the available transaction account options so the patched values remain selectable.
* If a lease has no template or omits the transfer accounts, existing debit/credit values remain unchanged.
* Users can still manually choose different accounts after the defaults are applied.

### Rationale

The guarded value-change handler speeds up data entry without blocking manual overrides. Prefilling only fires in response to a user changing the lease contract, keeping previously loaded rules stable while still providing template guidance for new selections.
