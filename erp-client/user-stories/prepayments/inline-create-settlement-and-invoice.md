# Create a missing Settlement or Invoice without losing the form you're already filling in

**Persona:** Finance/prepayments officer entering a Prepayment Account, referencing a Settlement
("Prepayment Transaction") by its DC payment number.

## Scenario 1: the Settlement doesn't exist yet

The officer is filling in a Prepayment Account, copying an existing one as a starting point, and
needs to change the "Prepayment Transaction" field from `DC685` to `DC317` - a payment number that
doesn't exist in the system yet, per the source spreadsheet.

### Steps

1. On the Prepayment Account form, search "DC317" in the Prepayment Transaction field.
2. See no match, and click "Create New".
3. Land on a Settlement create form with the Save button available (previously this button was
   missing entirely - Cancel was the only option).
4. Fill in the Settlement's required fields (Payment Category, Currency, Biller, etc.) and enter
   `DC317` as its payment number.
5. Save.

### Outcome

The officer is returned to the exact same Prepayment Account they were editing - not a blank form,
not the Settlement list - with the "Prepayment Transaction" field already filled in with the
Settlement they just created. Nothing else on the Prepayment Account form was lost.

## Scenario 2: the Settlement's Invoice doesn't exist yet either

While creating that same new Settlement (still inline, from the Prepayment Account above, or as a
standalone Settlement), the officer needs to attach an Invoice that also doesn't exist yet.

### Steps

1. On the Settlement form, search for the invoice number in the Invoices field.
2. See no match, and click "Create New" (previously this option didn't exist on this field at
   all).
3. Land on an Invoice create form with the Save button available.
4. Fill in the Invoice's details and save.

### Outcome

The officer is returned to the Settlement form they were filling in - with the new Invoice added
to its Invoices list - not to the Invoice list page. If that Settlement form was itself reached via
Scenario 1 (create-Settlement-from-Prepayment-Account), that outer context is preserved too: saving
the Settlement afterward still returns to the original Prepayment Account with the Settlement (now
carrying its new Invoice) filled in.

## Not yet covered

Cancelling out of the inline-create form (rather than saving) currently returns to wherever Cancel
already went (the list page) rather than back to the form that opened it - the underlying
`inlineCreateCancelled` action exists in the store for this case but nothing dispatches it yet.
