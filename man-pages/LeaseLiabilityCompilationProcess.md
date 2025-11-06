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
  - **Processor** – `LeaseLiabilityCompilationItemProcessor` delegates to the amortization service to produce schedule items per liability and forwards the compilation identifier so downstream DTOs are tagged correctly.
  - **Writer** – `LeaseLiabilityCompilationItemWriter` persists each list of schedule items through `InternalLeaseLiabilityScheduleItemService`, forcing the `active` flag to `true` and back-filling the compilation metadata when absent.

## Step 4 – Amortization schedule creation
- `LeaseAmortizationService` implements `LeaseAmortizationCompilationService` and drives the calculation pipeline.
- `generateAmortizationSchedule(Long leaseLiabilityId, Long compilationId)` performs:
  1. Retrieves the lease liability, associated IFRS16 contract, amortization calculation parameters, and amortization schedule metadata.
  2. Loads repayment periods and lease payments. Missing data triggers descriptive exceptions.
  3. Iterates through sorted periods, calculating interest accrual, interest payment, principal reduction, and outstanding balance.
  4. Builds `LeaseLiabilityScheduleItemDTO` records with balances, payment splits, lease period references, the supplied compilation identifier, and `active=true` defaults.

## Step 5 – Persisting results
- The batch writer stores each list of schedule items, linking them to the lease liability, contract, amortization schedule, and current compilation. Every row is pre-marked as active so reporting filters immediately recognise the new run.
- Once persisted, schedule items can be queried through existing reporting endpoints or exported for IFRS16 disclosures.

## Step 6 – Activate or deactivate compiled schedules
- Endpoints `POST /api/leases/lease-liability-compilations/{id}/activate` and `/deactivate` call `InternalLeaseLiabilityCompilationService.updateScheduleItemActivation`.
- The service executes a bulk `UPDATE` (`updateActiveStateByCompilation`) to flip the `active` flag for every schedule item linked to the compilation **and** updates the compilation’s own `active` flag. A fresh copy of the compilation is pushed to Elasticsearch while the related schedule items are reindexed in 200-item pages to avoid recursion limits, and JHipster alert headers include the compilation identifier plus affected row count payload.
- Use these toggles to promote a compilation to production reporting or to freeze a historical run without deleting data.

## Reporting recommendations for lease-period monitoring
To detect movement in a lease’s liability schedule for a specific repayment period, implement purpose-built reports that lean on
`LeaseLiabilityScheduleItemDTO` history and compilation metadata.

### 1. Lease-period schedule delta report
- **Business question** – How did the principal, interest, and outstanding balance change for a given lease period between two
  compilation runs?
- **Key filters** – Lease contract booking ID, lease liability ID, repayment period sequence/date range, compilation request ID.
- **Core measures** – Opening/closing balance, interest accrued, principal and interest payments, cash payment, interest payable
  opening/closing.
- **Implementation notes** – Join schedule items by `leaseLiabilityId` and `leasePeriod.id`, ordering by compilation timestamp.
  Display side-by-side comparisons and a delta column for each measure.

### 2. Lease-period variance vs. expected payment
- **Business question** – Does the actual cash payment applied in the schedule match the expected lease payment for the period?
- **Key filters** – Lease contract booking ID, repayment period identifier, payment date.
- **Core measures** – Scheduled cash payment, expected payment from `LeasePaymentDTO`, variance amount, variance percentage.
- **Implementation notes** – Blend schedule items with `LeasePaymentDTO` entries on period boundaries. Highlight outliers above a
  configurable tolerance and include drill-down links to payment records.

### 3. Compilation trend and exception dashboard
- **Business question** – Which lease periods required recalculation or triggered data-quality exceptions over time?
- **Key filters** – Compilation job execution date, lease liability ID, exception type, period sequence.
- **Core measures** – Number of compilations per lease period, count of failed/flagged periods, average time between recompilations.
- **Implementation notes** – Surface data from batch job tables (execution metadata, error logs) alongside schedule items. Provide
  visual cues (heat maps or sparklines) for periods that change frequently or produce repeated errors.

## Error handling and reruns
- If required data is missing, `LeaseAmortizationService` throws explicit `IllegalArgumentException`s identifying the missing component (liability, contract, calculation, payments, or schedule).
- Batch job failures are logged with contextual messages, enabling operators to rerun the job after data corrections.

## Operational considerations
- The batch job runs asynchronously and can be monitored via Spring Batch monitoring tools.
- Chunk size and parallelism can be tuned in `LeaseLiabilityCompilationBatchConfig` to accommodate larger portfolios.
- Ensure the asynchronous executor is sized to handle concurrent compilation requests without exhausting resources.
- Activation toggles operate independently of the batch job, enabling finance teams to switch between compilations quickly while keeping historical schedules intact for audit comparisons.
