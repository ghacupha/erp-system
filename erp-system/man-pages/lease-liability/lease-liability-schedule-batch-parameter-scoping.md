# Lease liability schedule batch job parameter scoping

## Summary
We resolved a startup failure in the lease liability schedule batch pipeline that occurred when Spring tried to inject `jobParameters` into singleton beans during application context initialization. The configuration now keeps job-parameter injection inside job/step-scoped beans so that the batch infrastructure only resolves these values when a job is actually executing.

## Key changes
- The `LeaseLiabilityScheduleUploadJobListener` is now `@JobScope`, allowing it to receive the `uploadId` job parameter through the batch context instead of at application boot.
- The composite processor delegates now include the dedicated, step-scoped metadata processor bean instead of manually invoking the factory method with static fields. This preserves the intended validation → lease-period resolution → row extraction → metadata enrichment flow while deferring job parameter resolution until step execution.
- Removed static `@Value` fields from the batch configuration to prevent SpEL evaluation of `jobParameters` during context startup.

## Rationale
Spring Batch only exposes `jobParameters` within job or step scopes. Injecting them into singletons (or resolving them eagerly) results in `BeanExpressionException` errors during application startup. By constraining the job-parameter-dependent components to batch scopes and wiring them as scoped beans inside the composite processor, the batch job can start successfully and still enrich each CSV row with the necessary metadata.
