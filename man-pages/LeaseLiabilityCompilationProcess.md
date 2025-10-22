# Lease Liability Compilation Process Guide

## Purpose
This guide describes how lease data travels through the ERP System to produce IFRS16-compliant amortization schedules. It highlights the REST API entry point, AOP interception, batch job orchestration, and the amortization calculation service that creates the final schedule items.

## Prerequisites
1. **Lease Contract Data** – An IFRS16 lease contract with a booking ID and commencement date must exist.
2. **Lease Liability** – Each contract has an associated liability containing the starting liability amount.
3. **Repayment Periods & Payments** – Repayment periods and expected payment amounts are recorded for the contract term.
4. **Amortization Calculation & Schedule** – Calculation parameters and a booking-level amortization schedule record are available.

## Step 1 – Submit a compilation request
- Endpoint: `POST /api/leases/lease-liability-compilations`.
- Body: `LeaseLiabilityCompilationDTO` without an `id` value.
- Behavior: `LeaseLiabilityCompilationResourceProd` validates and persists the request before returning a `201 Created` response containing the new compilation identifier.

## Step 2 – Interceptor launches the batch job
- `LeaseLiabilityCompilationRequestInterceptor` listens for successful responses from the POST resource method.
- After the response is returned, the interceptor logs the request, extracts the DTO, and invokes `LeaseLiabilityCompilationJob#compileLeaseLiabilitySchedule` asynchronously.

## Step 3 – Batch job configuration
- The job registered under `leaseLiabilityCompilationJob` is defined in `LeaseLiabilityCompilationBatchConfig`.
- The job runs a single step named `leaseLiabilityCompilationStep` with chunk size 24.
- Components:
  - **Reader** – `LeaseLiabilityCompilationItemReader` fetches liabilities adjacent to the compilation request using `InternalLeaseLiabilityService`.
  - **Processor** – `LeaseLiabilityCompilationItemProcessor` delegates to the amortization service to produce schedule items per liability.
  - **Writer** – `LeaseLiabilityCompilationItemWriter` persists each list of schedule items through `InternalLeaseLiabilityScheduleItemService`.

## Step 4 – Amortization schedule creation
- `LeaseAmortizationService` implements `LeaseAmortizationCompilationService` and drives the calculation pipeline.
- `generateAmortizationSchedule(Long leaseLiabilityId)` performs:
  1. Retrieves the lease liability, associated IFRS16 contract, amortization calculation parameters, and amortization schedule metadata.
  2. Loads repayment periods and lease payments. Missing data triggers descriptive exceptions.
  3. Iterates through sorted periods, calculating interest accrual, interest payment, principal reduction, and outstanding balance.
  4. Builds `LeaseLiabilityScheduleItemDTO` records with balances, payment splits, and lease period references.

## Step 5 – Persisting results
- The batch writer stores each list of schedule items, linking them to the lease liability, contract, and amortization schedule.
- Once persisted, schedule items can be queried through existing reporting endpoints or exported for IFRS16 disclosures.

## Error handling and reruns
- If required data is missing, `LeaseAmortizationService` throws explicit `IllegalArgumentException`s identifying the missing component (liability, contract, calculation, payments, or schedule).
- Batch job failures are logged with contextual messages, enabling operators to rerun the job after data corrections.

## Operational considerations
- The batch job runs asynchronously and can be monitored via Spring Batch monitoring tools.
- Chunk size and parallelism can be tuned in `LeaseLiabilityCompilationBatchConfig` to accommodate larger portfolios.
- Ensure the asynchronous executor is sized to handle concurrent compilation requests without exhausting resources.
