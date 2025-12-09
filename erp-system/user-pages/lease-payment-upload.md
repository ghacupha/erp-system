# Lease payment upload

1. Open **Leases → Lease Payment Upload**.
2. Verify the lease contract shown matches the payments in your CSV.
3. Leave **Launch batch immediately** enabled unless you intend to trigger indexing later.
4. Choose the CSV file (header columns: `paymentDate`, `paymentAmount`).
5. Submit the upload.

Successful uploads now return immediately without the `StackOverflowError` that previously appeared when indexing the CSV metadata. The record shows in the recent uploads table with its current status and progresses automatically as the batch job runs.
