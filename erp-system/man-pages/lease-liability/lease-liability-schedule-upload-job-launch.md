# Lease Liability Schedule Upload Job Launch Fix

## Background
The lease liability schedule upload endpoint stores metadata about the uploaded CSV file and optionally triggers the Spring Batch job defined in `LeaseLiabilityScheduleBatchJobConfiguration`. The existing implementation launched the batch job directly from the transactional `LeaseLiabilityScheduleUploadService`. Because the service method is annotated with `@Transactional`, the Spring Batch `JobRepository` detected the active transaction and aborted the launch with `Existing transaction detected in JobRepository`.

## Change Summary
* The service now defers launching the job until after the surrounding transaction commits. We register a `TransactionSynchronization` callback and invoke the `LeaseLiabilityScheduleUploadJobLauncher` inside `afterCommit()` so the job starts without an active transaction and after the upload metadata has been persisted.
* When no transaction is active (for example, if the service is invoked outside of a transactional boundary), the launcher falls back to executing immediately.

## Impact
This fix removes the IllegalStateException encountered when `launchBatchImmediately` is enabled, ensuring the upload metadata is committed before the batch job runs. The batch infrastructure operates with its own transaction boundaries as expected.
