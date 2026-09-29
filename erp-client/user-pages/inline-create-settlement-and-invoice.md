# How to: create a Settlement or Invoice inline, without losing your place

## Creating a Settlement while entering a Prepayment Account

1. On the Prepayment Account create/edit form, type into the **Prepayment Transaction** field.
2. If the Settlement you need doesn't show up in the search results, click **Create New**
   underneath the results list.
3. You'll land on a new Settlement form. Fill in the required fields (Payment Category, Currency,
   Biller, and so on) and click **Save**.
4. You'll be taken straight back to the Prepayment Account you were working on, with the
   Prepayment Transaction field already filled in with the Settlement you just created. Everything
   else you'd already entered on the form is still there.

## Creating an Invoice while entering a Settlement

Works the same way, one level in: on the Settlement form's **Invoices** field, search, and if
nothing matches, click **Create New** to open a new Invoice form. Saving it adds the new Invoice
to the Settlement's Invoices list and returns you to the Settlement form - including if that
Settlement form was itself opened via "Create New" from a Prepayment Account, in which case saving
the Settlement afterward still takes you all the way back to that Prepayment Account.

## Known limitation

Clicking **Cancel** instead of **Save** on an inline-created Settlement or Invoice does not return
you to the form you came from - it goes to that entity's list page instead, the same as opening
the create form directly would. Only **Save** completes the round trip back.
