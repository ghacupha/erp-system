# Prepayment Marshalling Account Reference Panel

## Purpose

The prepayment marshalling create form now gives the user a working reference panel for prepayment accounts that still need marshalling. The panel uses the existing unallocated prepayment account report endpoint instead of the aggregate prepayment-account-report endpoint, because the report rows identify actual prepayment account ids and expose the `amortizationEntryCount` used to isolate accounts with no amortization entries.

## Workflow

When the form opens, the client requests `api/prepayments/unallocated-prepayment-accounts` with a minimum outstanding amount of 1 and keeps only rows where `amortizationEntryCount` is zero. Selecting a row fetches the full prepayment account, patches the existing Prepayment Account form control, and keeps the direct search control available for manual lookup.

After a prepayment account is selected, the client fetches related business document references from the prepayment account and, when the account has a prepayment transaction, from the related settlement. Document references are deduplicated by id. Clicking a document title fetches the full business document and renders its file in an inline preview frame so the user can consult scanned amortization support without leaving the marshalling form.

## Notes

The PDF preview uses a browser object URL generated from the business document base64 payload and revokes the object URL when the preview is closed or the component is destroyed.
