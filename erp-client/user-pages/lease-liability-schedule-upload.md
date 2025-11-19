# Lease Liability Schedule Upload Guide

Follow these steps to import a lease liability schedule from CSV:

1. Open the lease liability schedules module and choose **Upload CSV**.
2. Use the type-ahead controls to select the lease liability, compilation batch, and (when available) the amortisation schedule that matches the file.
3. Confirm the IFRS16 contract that auto-populates and leave *Launch batch immediately* enabled unless you want to stage the file.
4. Attach the CSV file and submit the form. A green banner displays the upload id, CSV id, and status once the server accepts the payload.
5. Watch the upload status: `PENDING` → `PROCESSING` → `COMPLETED`. If the status changes to `FAILED`, inspect the error log, correct the CSV, and try again.
6. Refresh the schedule items grid to confirm the imported rows.

Refer to the in-app help for the expected column order when preparing the CSV file.
