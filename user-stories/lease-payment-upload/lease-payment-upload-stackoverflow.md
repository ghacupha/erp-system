# User story: Uploading lease payments without errors

**Persona:** Lease accountant preparing bulk payment schedules.

**Scenario:** The accountant selects a lease contract, keeps batch launch enabled, attaches the payment CSV, and submits the form.

**Expected outcome:**
* The upload succeeds instead of throwing a `StackOverflowError`.
* The newly submitted upload is visible in the recent uploads list with an initial status (`PENDING`/`PROCESSING`) while the batch job runs.
