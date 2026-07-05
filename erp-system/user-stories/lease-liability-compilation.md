# Server-Side User Stories – Lease Liability Compilation

## Technical Context
These user stories interpret the lease liability compilation workflow from the perspective of backend maintainers who operate the JHipster-powered ERP System service. They describe expectations on REST resources, AOP interceptors, batch infrastructure, and amortization services that collaborate to produce IFRS16-compliant schedules.

## Story A – Persist compilation requests via REST
**As a** backend maintainer  
**I want** `LeaseLiabilityCompilationResourceProd#createLeaseLiabilityCompilation` to persist incoming compilation requests  
**So that** batch processing can be triggered with the necessary metadata.

### Acceptance Criteria
- The resource validates that the incoming DTO has no identifier before delegating to `InternalLeaseLiabilityCompilationService.save`.  
- The method returns `ResponseEntity<LeaseLiabilityCompilationDTO>` with a generated identifier and creation headers.  
- The resource remains the single entry point for POST `/api/leases/lease-liability-compilations` calls.

## Story B – Intercept responses to launch compilation
**As a** backend maintainer  
**I want** `LeaseLiabilityCompilationRequestInterceptor` to capture successful POST responses  
**So that** `LeaseLiabilityCompilationJob` is launched asynchronously with relevant job parameters.

### Acceptance Criteria
- The interceptor uses the `reportRequisitionPointcut` to target the resource method.  
- After returning, the interceptor extracts the DTO, logs the request, and invokes `compileLeaseLiabilitySchedule`.  
- Invocation happens on a separate thread via the `@Async` annotation.

## Story C – Configure the batch job for compilation
**As a** backend maintainer  
**I want** `LeaseLiabilityCompilationBatchConfig` to orchestrate reader, processor, and writer beans  
**So that** liabilities adjacent to the compilation request are processed in deterministic chunks.

### Acceptance Criteria
- The job is registered under the `leaseLiabilityCompilationJob` name with a single step.  
- The step wires an `ItemReader` that fetches liabilities via `InternalLeaseLiabilityService#getCompilationAdjacentMetadataItems`.  
- The chunk size supports multi-period processing (currently `24`).

## Story D – Generate schedule items during processing
**As a** backend maintainer  
**I want** `LeaseLiabilityCompilationItemProcessor` to call `LeaseAmortizationCompilationService.generateAmortizationSchedule`  
**So that** each liability yields a complete list of `LeaseLiabilityScheduleItemDTO` entries.

### Acceptance Criteria
- The processor receives the batch job identifier for traceability.  
- Each processed liability returns schedule items that include contract, schedule, and lease period references.  
- Exceptions from missing data propagate meaningful messages for debugging.

## Story E – Persist the generated schedule items
**As a** backend maintainer  
**I want** `LeaseLiabilityCompilationItemWriter` to persist each list of schedule items  
**So that** amortization schedules become queryable after the batch run.

### Acceptance Criteria
- The writer delegates to `InternalLeaseLiabilityScheduleItemService.saveAll` for each liability’s schedule.  
- The writer supports repeated invocations within a single chunk and avoids duplicate persistence.

## Story F – Validate amortization inputs
**As a** backend maintainer
**I want** `LeaseAmortizationService` to guard against missing liabilities, contracts, calculations, payments, or schedules
**So that** amortization runs fail fast with actionable error messages.

### Acceptance Criteria
- `generateAmortizationSchedule` verifies the presence of each prerequisite entity and throws descriptive `IllegalArgumentException`s when absent.
- Payment matching respects repayment periods and defaults to zero when no payment is scheduled.
- Balances roll forward across periods, updating principal, interest, and outstanding amounts.

## Story G – Expose lease-period monitoring reports
**As a** backend maintainer
**I want** reporting APIs and data extracts that surface liability schedule changes for a selected lease period across compilations
**So that** downstream analytics and finance teams can monitor volatility and investigate anomalies.

### Acceptance Criteria
- Provide a reporting endpoint (or database view) that compares schedule item balances across compilation timestamps for the same `leaseLiabilityId` and `leasePeriod.id`.
- Join schedule items to `LeasePayment` records to calculate payment variance metrics and expose tolerance breach indicators.
- Persist batch job execution metadata necessary to trend recompilations and exception counts per lease period for dashboard consumption.

## Story H – Toggle active compilations for reporting
**As a** backend maintainer
**I want** REST endpoints that bulk-activate or bulk-deactivate schedule items belonging to a compilation
**So that** finance teams can promote a specific compilation to production reporting or freeze it for audit review without reprocessing schedules.

### Acceptance Criteria
- `LeaseLiabilityCompilationResourceProd` exposes `POST /api/leases/lease-liability-compilations/{id}/activate` and `/deactivate` guarded by lease roles.
- Each call validates that the compilation exists and delegates to `InternalLeaseLiabilityCompilationService.updateScheduleItemActivation`.
- The service executes `updateActiveStateByCompilation` on the repository, toggles the compilation's own `active` status, emits JHipster alert headers that include the compilation identifier, and reindexes affected schedule items for Elasticsearch parity using paged batches to avoid stack overflows.
- Batch-generated schedule items arrive with `active=true` and the compilation identifier already populated, so toggling only flips the boolean flag.
