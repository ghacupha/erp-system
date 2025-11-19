# Lease Liability Schedule Upload Guide

Follow these steps to import a lease liability schedule from CSV:

1. Open the lease liability schedules module and choose **Upload CSV**.
2. Use the type-ahead control to select the lease liability that the CSV belongs to. The server fetches the matching IFRS16 lease contract, creates the amortisation schedule and compilation context using that single selection, so no additional dropdowns are required.
3. Attach the CSV file, decide whether *Launch batch immediately* should stay enabled, and submit the form. A green banner displays the upload id, CSV id, and status once the server accepts the payload.
4. Watch the upload status: `PENDING` → `PROCESSING` → `COMPLETED`. If the status changes to `FAILED`, inspect the error log, correct the CSV, and try again.
5. Refresh the schedule items grid to confirm the imported rows.

Refer to the in-app help for the expected column order when preparing the CSV file.
