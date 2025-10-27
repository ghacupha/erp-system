# User Guide: Configurable Report Filters

Administrators can configure dynamic reporting filters directly from the server metadata catalogue. The `/api/report-metadata`
endpoint now returns a `filters` collection for each report entry. Follow the steps below when consuming these descriptors in
client applications:

1. **Load metadata** – Request the report metadata list or search endpoint. Each report contains ordered filter descriptors.
2. **Render controls** – For every descriptor, create a selector using the provided `label` and `uiHint`. The UI hint helps
   choose between dropdowns, typeaheads, or other control styles.
3. **Fetch options** – Use the `valueSource` token to decide which catalogue API to call (e.g., lease periods, prepayment
   accounts).
4. **Build queries** – When the user selects values, send them back using the provided `queryParameterKey`. Keys already include
   filter operators such as `.equals` to match backend expectations.
5. **Support ordering** – Preserve the descriptor order delivered by the API so the layout remains consistent with the curated
   experience seeded on the server.

By relying on the descriptors, front-end teams no longer need hard-coded lease-specific toggles, enabling the same UI workflow to
serve leases, prepayments, and amortisation reports.
