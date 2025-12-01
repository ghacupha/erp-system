# User story: Uploading and correcting lease payments in bulk

## Persona
Lease accountant responsible for onboarding IFRS16 leases and maintaining accurate amortisation schedules.

## Goal
Load a full set of lease payments from a CSV prepared during onboarding, monitor the upload status, and deactivate an incorrect upload so that a fresh file can be ingested before regenerating the amortisation schedule.

## Steps
1. Navigate to **Lease Payment Upload** from the Lease module menu.
2. Select the relevant IFRS16 lease contract in the contract picker.
3. Choose the prepared CSV file containing `paymentDate` and `paymentAmount` rows and keep **Launch batch immediately** checked.
4. Submit the upload and wait for the success banner showing the upload id, stored filename, and status.
5. Review the **Previous uploads** table to confirm the new row is marked Active with status **PENDING/COMPLETED**.
6. If a mistake is discovered (e.g., payments misaligned with the lease), click **Deactivate** on the erroneous upload; the table will refresh showing Active = No.
7. Upload a corrected CSV for the same contract. When the amortisation schedule is recompiled, only payments from the active upload are used.

## Expected outcome
* Payments from the uploaded CSV are persisted and flagged active, ready for inclusion in schedule compilation.
* Deactivating an upload automatically suppresses all related lease payments from subsequent amortisation runs.
* The uploads table provides traceability (id, contract, file name, status, active flag) for audit and troubleshooting.
