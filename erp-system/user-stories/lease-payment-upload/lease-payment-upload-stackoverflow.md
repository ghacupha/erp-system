# User story: Uploading lease payments without errors

**Persona:** Lease accountant preparing bulk payment schedules.

**Goal:** Upload a CSV of periodic lease payments and have the job start immediately without backend failures.

**Steps:**
1. Navigate to *Leases → Lease Payment Upload*.
2. Confirm the target lease contract is displayed.
3. Leave "Launch batch immediately" checked.
4. Choose the prepared CSV file and submit the upload.

**Expected result:**
* The upload request returns successfully instead of failing with a `StackOverflowError`.
* The upload appears in the recent uploads list with status `PENDING` or `PROCESSING` before batch completion.
