# Lease Liability Compilation – Backend Process Notes

## Scope
This manual documents the backend workflow that produces IFRS16 lease amortization schedules after a compilation request is submitted. It is tailored for engineers maintaining the ERP System server module.

## Data Flow Summary
1. **REST intake** – `LeaseLiabilityCompilationResourceProd` persists the incoming `LeaseLiabilityCompilationDTO` and returns the created entity.
2. **AOP interception** – `LeaseLiabilityCompilationRequestInterceptor` intercepts successful responses, logs execution, and forwards the DTO to the compilation job.
3. **Batch job kick-off** – `LeaseLiabilityCompilationJobImpl` constructs Spring Batch `JobParameters` (job token, batch identifier, request ID) and launches the `leaseLiabilityCompilationJob`.
4. **Chunk-oriented processing** – `LeaseLiabilityCompilationBatchConfig` wires the reader, processor, and writer beans that handle liabilities in chunks of 24.
5. **Schedule calculation** – `LeaseAmortizationService` aggregates liability, contract, calculation, repayment period, and payment data to build `LeaseLiabilityScheduleItemDTO` results.
6. **Persistence** – `LeaseLiabilityCompilationItemWriter` commits the generated schedule items using `InternalLeaseLiabilityScheduleItemService.saveAll`.

## Detailed Components
### REST Resource
- Validates that incoming DTOs do not carry an ID and delegates persistence to `InternalLeaseLiabilityCompilationService`.
- Exposes the POST route under `/api/leases/lease-liability-compilations`.
- Other CRUD endpoints remain available for querying compilations after processing.

### Interceptor
- Pointcut: `execution(* io.github.erp.erp.resources.leases.LeaseLiabilityCompilationResourceProd.createLeaseLiabilityCompilation(..))`.
- Advice: `@AfterReturning` obtains the saved DTO, logs context, and invokes `compileLeaseLiabilitySchedule` on the job interface.
- Runs asynchronously to avoid blocking the HTTP response lifecycle.

### Batch Infrastructure
- `LeaseLiabilityCompilationItemReader` pulls liabilities tied to the compilation request and batch identifier.
- `LeaseLiabilityCompilationItemProcessor` transforms each liability into a list of schedule items by calling the amortization service.
- `LeaseLiabilityCompilationItemWriter` iterates over each list and saves it in bulk.

### Amortization Logic
- `generateAmortizationSchedule` checks for the existence of the liability, contract, calculation, payments, and amortization schedule records.
- It derives a monthly rate from the calculation’s interest rate, aligns payments with repayment periods, and maintains running balances for interest and principal.
- Each `LeaseLiabilityScheduleItemDTO` captures opening balance, cash payment, split between interest and principal, outstanding balance, and links to the lease period and amortization schedule metadata.

## Operational Guidance
- Monitor job executions via Spring Batch tooling; job parameters include the batch identifier for correlation.
- Missing prerequisite data surfaces as `IllegalArgumentException`s—capture these in logs or monitoring dashboards to inform data stewards.
- Adjust chunk size or introduce parallel steps in `LeaseLiabilityCompilationBatchConfig` when scaling to larger portfolios.

## Reporting recommendations for lease-period monitoring
Engineering teams can expose focused reports to track how schedule items evolve for a specific lease period and compilation run.

### Lease-period schedule delta view
- **Objective** – Compare principal, interest, and outstanding balances for the same lease period across sequential compilation runs.
- **Suggested inputs** – Lease contract booking ID, lease liability ID, repayment period ID/sequence, compilation timestamp.
- **Data points** – Opening/closing balances, interest accrued, interest/principal payment split, cash payment, interest payable opening/closing.
- **Implementation hint** – Query `LeaseLiabilityScheduleItem` entities joined on `leasePeriod.id` and `leaseLiability.id`, sorted by compilation execution time, and compute deltas across runs.

### Lease-period payment variance audit
- **Objective** – Validate that schedule cash payments align with expected lease payments for the period.
- **Suggested inputs** – Lease contract booking ID, repayment period boundaries, payment date, compilation status.
- **Data points** – Schedule cash payment, expected payment sourced from `LeasePayment` records, absolute/percentage variance, tolerance indicator.
- **Implementation hint** – Blend schedule items with `LeasePayment` entries using date overlap logic from `LeaseAmortizationService`. Highlight exceptions exceeding tolerance to prompt data stewardship.

### Compilation trend and exception dashboard
- **Objective** – Surface lease periods that frequently change or fail due to missing inputs.
- **Suggested inputs** – Compilation job execution date, lease liability ID, exception category, period sequence, batch identifier.
- **Data points** – Count of compilations per period, number of failures or warning flags, average time between recompilations, outstanding exception age.
- **Implementation hint** – Combine Spring Batch execution metadata with amortization service logs. Use heat maps or sparklines to visualize volatile periods and drive remediation.

## Related Artifacts
- User stories describing this workflow are stored in `../user-stories/lease-liability-compilation.md` and `../erp-system/user-stories/lease-liability-compilation.md`.
- Source implementations reside under `io.github.erp.erp.resources.leases`, `io.github.erp.aop.lease`, and `io.github.erp.internal.service.leases` packages.
