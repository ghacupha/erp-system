# Lease Template UI Workflows

## Purpose
Lease templates consolidate recurring IFRS16 lease configuration into a reusable parent entity. The UI supports creating and copying templates, then selecting them on lease contracts so shared data (service outlet, main dealer) is reused when contracts renew.

## Key Components
- `LeaseTemplateModule` and CRUD components provide list, detail, update, and delete views.
- NgRx workflow state tracks create/edit/copy flows, mirroring existing lease entity patterns.
- Custom form controls (transaction accounts, asset category, service outlet, dealer) keep relationship selection consistent with other lease forms.
- `M21LeaseTemplateFormControlComponent` exposes a reusable NgSelect-based lookup for selecting templates on the IFRS16 lease contract update form.

## Workflow Summary
1. Users create a lease template, filling in template title plus transaction account mappings, asset category, service outlet, and main dealer.
2. Users can copy an existing template from the list or detail view to speed up setup for similar lease classes.
3. Lease contracts select a template, which populates service outlet and main dealer defaults during contract updates.

## Design Decisions
- Reused existing NgRx workflow patterns to keep copy/edit behaviors consistent with ROU metadata and direct cost flows.
- Used the shared form control components to avoid duplicating lookup logic for transaction accounts and reference entities.
- Kept template title required to provide a clear anchor when selecting templates during contract updates.
