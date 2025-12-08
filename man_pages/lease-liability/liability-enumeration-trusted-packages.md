# Liability Enumeration Batch Context Deserialization

## Summary
Liability enumeration batch executions were failing with `Unable to deserialize the execution context` because the execution context contained an instance of `LiabilityEnumerationResponse`, which was not included in the trusted package list for Jackson's batch serializer.

## Root Cause
Spring Batch's `Jackson2ExecutionContextStringSerializer` rejects types that are not explicitly trusted. The amortization step stores the enumeration response in the job execution context, but no trusted package configuration existed for the `io.github.erp.erp.leases.liability.enumeration` classes. When the job restarted or queried its last execution, deserialization failed and the job launcher raised an exception.

## Fix
A custom `ExecutionContextSerializer` bean now configures `Jackson2ExecutionContextStringSerializer` with trusted packages that include `io.github.erp.erp.leases.liability.enumeration`. This allows the stored `LiabilityEnumerationResponse` to be deserialized safely while still limiting other packages.

## Impact
- Liability enumeration jobs can be launched and resumed without deserialization errors.
- Trust remains limited to the specific domain package plus standard Java types.
