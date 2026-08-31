# Asset Registration detail page — denser layout and direct document access

## Business purpose

The Asset Registration detail view (`/asset-registration/:id/view`) is the page a
fixed-assets officer lands on to inspect a single registered asset. It previously rendered
every field of the entity (~28 of them) as a full-width row in a single JHipster
`<dl class="row-md jh-entity-details">` list. Reading one asset therefore meant scrolling
through a long, sparse column. There was also no way to reach the actual supporting
document (invoice, purchase agreement, delivery note scan, …) from this page — the
"Business Document" row only linked to `/business-document/:id/view`.

This change:

1. Reorganises the same fields into titled panels on a two-column CSS grid so the whole
   record is visible with little or no scrolling.
2. Adds a **Related Documents** panel that lists every business document connected to the
   asset, previews a selected document inline in an `<iframe>`, and offers an
   "open in new tab" link that streams the file to the browser.

No fields were removed and no entity definitions changed.

## Where the documents come from

A business document relevant to an asset can be attached in three places:

| Source | Field | Notes |
|---|---|---|
| The AssetRegistration itself | `assetRegistration.businessDocuments` | many-to-many |
| The Acquiring Transaction | `assetRegistration.acquiringTransaction` → `Settlement.businessDocuments` | required many-to-one to Settlement |
| Any other related settlement | `assetRegistration.otherRelatedSettlements[]` → `Settlement.businessDocuments` | many-to-many |

The `GET api/fixed-asset/asset-registrations/{id}` payload flattens its nested relations:
the asset's own `businessDocuments` come back with `id` + `documentTitle` only, and the
nested settlements come back as `{ id, paymentNumber }` with **no** documents. So the
component fans out:

- collects the distinct settlement ids (`acquiringTransaction.id` plus every
  `otherRelatedSettlements[].id`),
- issues one `GET api/payments/settlements/{id}` per id (via `SettlementService.find`,
  in parallel with `forkJoin`, each guarded by `catchError` so one failure does not sink
  the rest),
- merges the asset's own documents with every settlement's `businessDocuments`,
  de-duplicating by document id.

The response for the acquiring settlement is also reused to populate the existing
"Acquiring Transaction" summary line, replacing the standalone `SettlementService.find`
call the component used to make in `ngOnInit`.

Opening or previewing a file needs the bytes, which only
`GET api/docs/business-documents/{id}` returns (a server-side AOP interceptor attaches the
base64 `documentFile` from disk). `selectBusinessDocument()` fetches that, builds a Blob
and shows it via `DomSanitizer.bypassSecurityTrustResourceUrl` in the preview iframe;
`openBusinessDocumentFile()` fetches the same and hands it to the shared
`DataUtils.openFile()` helper (Blob + `window.open`).

## Key files

- `erp-client/src/main/webapp/app/erp/erp-assets/asset-registration/detail/asset-registration-detail.component.ts`
- `erp-client/src/main/webapp/app/erp/erp-assets/asset-registration/detail/asset-registration-detail.component.html`
- `erp-client/src/main/webapp/app/erp/erp-assets/asset-registration/detail/asset-registration-detail.component.scss` (new)
- `erp-client/src/main/webapp/app/erp/erp-assets/asset-registration/detail/asset-registration-detail.component.spec.ts`

## Design decisions

- **Reuse over new code.** The layout, the SCSS grid, and the document
  load/merge/preview logic are ported almost verbatim from the already-reworked
  `PrepaymentAccountDetailComponent`
  (`app/erp/erp-prepayments/prepayment-account/detail/`), which solved the identical
  problem for prepayment accounts. `DataUtils.openFile`, `SettlementService.find` and
  `BusinessDocumentService.find` were all already in the codebase.
- **All fields kept.** Panels are: *Identification*, *Financials & Dates*,
  *Classification & Location*, *Acquisition & Procurement*, *Related Documents*
  (full width), *Supporting Data* (full width). The grid collapses to a single column
  below ~992px.
- **`otherRelatedSettlements` included.** The team asked for documents from *all* related
  settlements, not just the acquiring transaction, accepting one extra HTTP call per
  related settlement.
- **No backend change.** Existing endpoints already return everything needed.

## Verification / limitations

- `npm run lint` and `npm run prettier:check` pass for the touched files.
- `ng build --configuration development` compiles the reworked template and new SCSS
  cleanly.
- Jest specs (`asset-registration-detail.component.spec.ts`) were updated with mock
  `SettlementService` / `BusinessDocumentService` providers and a case covering the
  multi-settlement merge. Note: the Jest runner cannot execute locally on Node 22 because
  the pinned `node-sass@6` binary does not support that runtime — the pre-existing
  `prepayment-account-detail` spec fails identically. The suite must be run in CI or on a
  supported Node version.
