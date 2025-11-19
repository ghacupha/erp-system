# Uploading a Lease Liability Schedule from CSV

**As a** financial accountant, **I want** to upload a prepared amortisation schedule so that the ERP system can persist each period as `LeaseLiabilityScheduleItem` records without manual data entry.

## Scenario
1. I open the lease liability schedule workspace and choose **Upload CSV**.
2. Using the m21 selectors I pick the lease liability, the automatically suggested IFRS16 contract, the compilation request and—when needed—the amortisation schedule that should own the rows.
3. I attach the CSV exported from Excel, leave the *Launch batch immediately* checkbox enabled, and submit.

## Acceptance Criteria
- The request is accepted and recorded with an initial `PENDING` status; the UI surfaces the upload id, CSV id and status returned by the API.
- A background job parses the CSV, sends the rows to the queue, and the status transitions to `COMPLETED` when all rows are stored.
- If any row fails validation the status changes to `FAILED` and the error log references the problematic row so I can correct the file.
