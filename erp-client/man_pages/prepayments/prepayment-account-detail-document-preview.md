# Prepayment account detail layout and document preview

## Why this changed

The prepayment-account detail page previously stretched the account fields into a long single-column description list. That made it harder to scan the core accounting data and made the related document links easy to miss.

## What the page now does

The detail view now groups the account into clearer sections:

1. Account details such as recognition date, posting date, currency, dealer, settlement transaction, and accounts.
2. Related documents, gathered from both the prepayment-account record itself and the settlement linked through `prepaymentTransaction`.
3. Supporting data such as placeholders and parameter mappings.

When a user selects a related business document, the form loads the full document record and renders the PDF in an inline preview frame. This lets the user compare the scanned source document with the account values without leaving the detail page.

## Notes

The document list is de-duplicated by document id so the same file is not shown twice if it is reachable through both the account and the related settlement.
