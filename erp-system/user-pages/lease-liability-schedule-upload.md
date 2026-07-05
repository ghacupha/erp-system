# Lease Liability Schedule Upload Guide

1. Navigate to **Lease Liabilities → Schedules** and click **Upload CSV**.
2. Select the lease liability. The backend now looks up the IFRS16 contract, spins up a fresh amortisation schedule, and prepares a compilation context, so no additional drop-downs are required.
3. Review the automatically populated compilation identifier for traceability.
4. Click **Choose File**, select the CSV generated from your spreadsheet, and submit the form.
5. Monitor the upload record:
   - `PENDING` indicates the batch job has been queued.
   - `PROCESSING` means the CSV is being parsed.
   - `COMPLETED` confirms all rows were imported.
   - `FAILED` indicates at least one row could not be processed; review the error details before retrying.
6. Once completed, refresh the schedule items list to review the imported instalments.

**Tip:** Ensure the CSV headers match the system template so that numeric fields (balances, payments, period identifiers) can be parsed without manual correction.
