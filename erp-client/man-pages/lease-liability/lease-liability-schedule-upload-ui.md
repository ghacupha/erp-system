# Lease Liability Schedule Upload UI

## Overview
The Angular upload form lives under **ERP → Leases → Lease Liability Schedule Upload**. It wraps the custom
`/api/leases/lease-liability-schedule-file-uploads` endpoint and lets a lease manager pick the correct context before posting the CSV
file. The component relies on the reusable `m21` controls so that every lookup behaves consistently with the rest of the ERP UI.

## Form controls
1. **Lease liability** – type‑ahead lookup driven by `m21-lease-liability-form-control`. This is the only relationship the user
   needs to supply; the backend now builds the amortisation schedule and compilation context once it receives the request.
2. **CSV file chooser** – native `<input type="file">` limited to `.csv` MIME types. The chooser is accompanied by helper text
   that echoes the selected file name.
3. **Launch batch immediately** – boolean toggle that mirrors the `launchBatchImmediately` flag on the request payload. Most
   uploads keep it enabled so the batch processor starts as soon as the file is persisted.

## Upload lifecycle
1. The user picks the lease liability and CSV file.
2. Clicking **Submit Upload** serialises the lease identifier into the `request` part of a `FormData` payload and attaches the file
   to the `file` part.
3. The component shows an inline success card with the upload id, CSV id and status returned by the server. The card can be
   dismissed with the *Clear* link.
4. If the backend rejects the file, the error is surfaced in the dedicated alert banner so the user can fix the problem before
   retrying.

## File validation
The UI only checks that a file is present; all other validations (sequence numbers, repayment period matching, etc.) continue to
run inside the Spring Batch pipeline described in the server man-page. This keeps the client responsive while still surfacing the
processing status immediately after submission.
