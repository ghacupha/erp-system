# User story: Deactivate a lease payment upload

- **Persona:** Lease accountant safeguarding payment schedules ahead of reporting.
- **Goal:** Disable an uploaded batch of lease payments so that neither operational listings nor search results expose payments that should be withdrawn.
- **Journey:**
  1. Open the lease payment upload detail and choose the deactivate action for the batch.
  2. The system marks the upload as deactivated and flips the `active` flag on every payment created from the file.
  3. A background reindex runs after the database updates, ensuring Elasticsearch-powered searches stop returning those payments or show them as inactive.
- **Outcome:** The batch and its payments remain in the system for audit purposes but are excluded from active search flows and downstream processing.
