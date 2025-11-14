# Story: Uploading a Lease Liability Schedule from CSV

## Persona
- **Role:** Financial Accountant
- **Goal:** Import a lease liability amortisation schedule generated offline so that the ERP system can calculate balances and disclosures automatically.

## Steps
1. Navigate to the lease liability schedules section and click **Upload CSV**.
2. Select the lease liability and (optionally) the amortisation schedule using the drop-down selectors.
3. Confirm the automatically generated compilation batch and attach the CSV file exported from Excel.
4. Submit the upload request.

## Expected Outcome
- The file is stored and the upload request appears with a `PENDING` status that transitions to `PROCESSING`.
- Within moments the status becomes `COMPLETED` and the imported rows are visible in the schedule item list for the selected lease liability.
- If validation fails, the status changes to `FAILED` and the accountant can review error logs to correct the CSV.
