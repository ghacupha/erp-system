# Uploading a Lease Liability Schedule from CSV

**As a** financial accountant, **I want** to upload a prepared amortisation schedule so that the ERP system can persist each period as `LeaseLiabilityScheduleItem` records without manual data entry.

## Scenario
1. I open the lease liability schedule workspace and choose **Upload CSV**.
2. I select the relevant lease liability, optionally pick an amortisation schedule, and confirm the compilation batch that the system has generated.
3. I attach the CSV exported from Excel and submit.

## Acceptance Criteria
- The request is accepted and recorded with an initial `PENDING` status.
- A background job parses the CSV, sends the rows to the queue, and the status transitions to `COMPLETED` when all rows are stored.
- If any row fails validation the status changes to `FAILED` and the error log references the problematic row so I can correct the file.
