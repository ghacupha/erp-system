# User story — Open a business document from an asset's registration page

## Persona

**Priya, a fixed-assets officer.** She reviews registered assets and frequently needs to
check the source paperwork — the supplier invoice, the purchase agreement, the delivery
note scan — that backs an asset. The document is sometimes attached to the asset record and
sometimes to the settlement (payment) that acquired it.

## Story

> As a fixed-assets officer, when I open an asset's registration page I want to see the
> whole record at a glance and open any supporting document without leaving the page, so I
> can verify an asset quickly.

## Steps

1. Priya goes to **Registry → Asset Registration**, searches for the asset, and clicks it
   to open `/asset-registration/{id}/view`.
2. The page shows the asset in grouped panels — *Identification*, *Financials & Dates*,
   *Classification & Location*, *Acquisition & Procurement* — with the full record visible
   without scrolling on a normal screen.
3. In the **Related Documents** panel she sees a row of buttons, one per document. The list
   combines documents attached directly to the asset and documents attached to its
   Acquiring Transaction and any other related settlements, with no duplicates.
4. She clicks a document button. The document loads into an inline preview frame below the
   list.
5. To open the file full-size she clicks **open in new tab** in the preview header; the
   file opens in a new browser tab.
6. If the asset has no related documents, the panel says
   "No related business documents were found."

## Expected outcome

- Every field previously on the page is still present, now grouped into panels.
- The Related Documents list is de-duplicated across the asset and all related settlements.
- Selecting a document previews it inline; "open in new tab" opens the actual file.
- Back, Edit and Copy behave exactly as before (Edit/Copy still start the register-update
  workflow).

## Verified

- Layout, panel collapse (single column below ~992px), and clean compile confirmed via
  `ng build --configuration development`, `npm run lint`, `npm run prettier:check`.
- Multi-settlement document merge covered by a unit test in
  `asset-registration-detail.component.spec.ts`.

## Known limitation

The Jest test suite cannot run on the current local Node runtime (pinned `node-sass@6`
does not support Node 22); the same limitation pre-dates this change. Run the spec in CI or
on a supported Node version.
