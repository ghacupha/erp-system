# Lease Liability Schedule Upload UI

## Overview
The Angular upload form lives under **ERP → Leases → Lease Liability Schedule Upload**. It wraps the custom
`/api/leases/lease-liability-schedule-file-uploads` endpoint and lets a lease manager pick the correct context before posting the CSV
file. The component relies on the reusable `m21` controls so that every lookup behaves consistently with the rest of the ERP UI.

## Form controls
1. **Lease liability** – type‑ahead lookup driven by `m21-lease-liability-form-control`. Selecting a liability automatically
   fills the IFRS16 lease contract control with the linked contract.
2. **Lease amortization schedule** – optional lookup that lets the user bind an existing schedule. It uses the new
   `m21-lease-amortization-schedule-form-control` and searches against the `_search/lease-amortization-schedules` endpoint.
3. **Lease liability compilation** – required control wired to `m21-lease-liability-compilation-form-control`. It ensures the upload
   is tied to the compilation batch that will consume the data.
4. **IFRS16 lease contract** – reuses the standard `m21-ifrs16-lease-form-control`. Users can override the auto selection if
   they need to switch contracts.
5. **CSV file upload metadata** – read-only `m21-csv-file-upload-form-control`. After a successful upload the component injects the
   returned `csvFileId` and stored file name so users can immediately see which metadata record was created.
6. **CSV file chooser** – native `<input type="file">` limited to `.csv` MIME types.
7. **Launch batch immediately** – boolean toggle that mirrors the `launchBatchImmediately` flag on the request payload.

## Upload lifecycle
1. The user fills the required relationships and picks a CSV file.
2. Clicking **Submit Upload** serialises the lease identifiers into the `request` part of a `FormData` payload and attaches the file
   to the `file` part.
3. The component shows an inline success card with the upload id, CSV id and status returned by the server. The card can be
   dismissed with the *Clear* link.
4. If the backend rejects the file, the error is surfaced in the dedicated alert banner so the user can fix the problem before
   retrying.

## File validation
The UI only checks that a file is present; all other validations (sequence numbers, repayment period matching, etc.) continue to
run inside the Spring Batch pipeline described in the server man-page. This keeps the client responsive while still surfacing the
processing status immediately after submission.
