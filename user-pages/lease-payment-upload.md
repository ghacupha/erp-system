# Lease payment upload

Follow these steps to submit payment CSV files without backend errors:

1. Navigate to **Leases → Lease Payment Upload**.
2. Confirm the displayed lease contract matches the CSV content.
3. Keep **Launch batch immediately** selected for automatic processing.
4. Attach the CSV (`paymentDate`, `paymentAmount` columns) and submit.

The upload now completes without the prior `StackOverflowError`; the entry appears in the uploads list and progresses through batch processing automatically.
