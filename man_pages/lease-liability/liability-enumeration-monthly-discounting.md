# Liability enumeration monthly discounting and search updates

## Context

The liability enumeration flow now aligns discounting with the monthly reporting cadence even when lease cashflows settle quarterly, bi-annually or annually. The lease payment upload entity is also searchable so that NgSelect-driven form controls can retrieve uploads by contract at runtime.

## Changes

- Discounting is forced to monthly compounding in the present value calculator. Months without settlements between payment dates are filled with zero-value cashflows so that amortization aligns with monthly reporting.
- Liability time granularity now supports a `BI_ANNUAL` code for contracts that settle twice a year, while still applying monthly discounting.
- Lease payment uploads are indexed in Elasticsearch when created or when their status toggles, and a startup indexing service rebuilds the search index so NgSelect components can find uploads immediately after a restart.
- A new search endpoint (`/api/leases/_search/lease-payment-uploads`) exposes lease payment uploads with optional contract filtering for suggestion lookups.

## Rationale

Monthly discounting avoids plug adjustments when amortizing liabilities on a monthly schedule but ingesting non-monthly settlements. Zero-value interim months keep the discount ladder in sync with the amortization cadence. Search indexing for lease payment uploads allows the liability enumeration form to reuse NgSelect controls rather than manual numeric input.

## Implications

- Existing quarterly or annual uploads now produce monthly present value lines; reporting should show smooth monthly discount factors without end-of-term plugs.
- Search indices must be available for lease payment uploads; the new startup service handles rebuilds automatically but deployments should still ensure Elasticsearch is reachable.
- Front-end forms should rely on the new NgSelect controls for lease contracts and payment uploads; numeric ID inputs are deprecated in the enumeration form.
