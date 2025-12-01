# Lease payment upload deactivation

This guide covers turning off a batch of lease payments after it has been uploaded.

## Steps
1. Open the lease payment upload you want to disable.
2. Choose **Deactivate**. The upload status changes to **DEACTIVATED** and the system flips the `active` flag on every payment created from that file.
3. A background reindex runs automatically after the database update, so search endpoints and dashboards stop returning the inactive payments or show them as inactive.

## Expected results
- The upload is marked inactive for audit purposes.
- Individual payments remain stored but are no longer active in queries that respect the `active` flag.
- Search-backed views update shortly after deactivation completes.
