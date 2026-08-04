# Attach accessories and invoices to a fixed asset without the save failing

**Persona:** Fixed-assets officer completing an asset registration in erp-client after the asset
number, category, dealer and acquiring transaction are already saved.

## Scenario

The officer opens an existing `AssetRegistration` (e.g. a newly registered laptop) and starts
linking it to everything else that documents it: its keyboard/monitor accessories, the supplier
invoice(s) that paid for it, maybe a delivery note or warranty. Previously, saving these links
would sometimes fail outright with a server error, because the update was written straight into
Elasticsearch - as the full, deeply-nested `AssetRegistration` record - inside the same request
that saved the database row.

## Steps

1. Open the asset's detail/edit page in erp-client.
2. Under **Asset Accessories**, select the keyboard and monitor that came with it.
3. Under **Payment Invoices**, select the supplier invoice(s).
4. Save.

## Outcome

The save succeeds and returns immediately - it no longer depends on Elasticsearch being
available or able to accept the resulting document. The asset's search index entry updates
shortly after (queued, not immediate) and reflects the newly attached accessories and invoices
once it does; searching for the asset by accessory description or invoice number will find it
once the index catches up. If a search happens in the brief window before that, it just won't
appear in results yet - the underlying record and its relationships are correct in the database
either way, and re-opening the asset's own detail page (which reads the database directly, not
the search index) shows them immediately.
