# Lease Liability Compilation – User Stories

## Overview
This collection of user stories documents how finance and accounting stakeholders interact with the lease liability compilation workflow. It focuses on the journey from capturing IFRS16 lease data to producing a detailed amortization schedule through the automated compilation batch job.

## Personas
- **Finance Data Steward** – maintains IFRS16 lease contracts, liabilities, and repayment parameters.
- **Accounting Specialist** – initiates the compilation and validates the amortization schedule output.
- **System Administrator** – monitors background jobs and addresses integration issues.

## Story 1 – Capture prerequisite lease data
**As a** Finance Data Steward  
**I want** to register an IFRS16 lease contract, its associated lease liability, repayment periods, and expected payments  
**So that** the system has complete data when generating amortization schedules.

### Acceptance Criteria
- Contract metadata (booking ID, commencement date, etc.) is stored before liabilities are created.
- Lease liabilities reference the correct contract and hold the initial liability amount.
- Repayment periods and payment expectations exist for each contract term.

## Story 2 – Request a lease liability compilation
**As an** Accounting Specialist  
**I want** to submit a POST request to `/api/leases/lease-liability-compilations` with a new `LeaseLiabilityCompilationDTO`  
**So that** the platform can generate amortization schedules for the targeted liabilities.

### Acceptance Criteria
- The request payload omits an `id`, allowing the resource to persist the compilation request.
- A successful response returns a `201 Created` status with the generated compilation identifier.
- The response is intercepted by the AOP interceptor, which immediately delegates compilation to the asynchronous job.

## Story 3 – Monitor asynchronous processing
**As a** System Administrator  
**I want** the interception layer to launch `LeaseLiabilityCompilationJob` with traceable job parameters  
**So that** I can monitor batch execution and correlate results with the originating request.

### Acceptance Criteria
- A batch job identifier and request ID are included in the job parameters.
- The job uses a reader, processor, and writer to hydrate liabilities, calculate schedules, and persist results.
- Failures are logged with diagnostic messages for reruns or support analysis.

## Story 4 – Produce amortization schedule items
**As an** Accounting Specialist  
**I want** each liability processed by the batch job to yield a list of `LeaseLiabilityScheduleItemDTO` records  
**So that** I can review period-by-period interest, principal, and cash flow movements.

### Acceptance Criteria
- The item processor invokes the amortization service to build schedule items using the liability, contract, calculation, and payment data.
- Each schedule item stores opening balance, interest accruals, payment splits, and the associated repayment period.
- The writer persists every generated schedule item set to the underlying schedule repository.

## Story 5 – Handle missing or inconsistent data
**As a** Finance Data Steward  
**I want** the compilation service to alert me when required data is missing (e.g., payments or schedules)  
**So that** I can correct the source data and resubmit the compilation.

### Acceptance Criteria
- The amortization service raises descriptive errors when liabilities, contracts, calculations, payments, or schedules are missing.
- Compilation requests that fail validation are logged with enough context for troubleshooting.
- After correcting data, the user can resubmit the compilation request and receive a refreshed schedule.

## Story 6 – Review compiled schedules
**As an** Accounting Specialist  
**I want** to retrieve the generated schedule items linked to the compilation request  
**So that** I can validate amortization figures against IFRS16 reporting requirements.

### Acceptance Criteria
- Schedule items persist with references back to the originating liability and contract.
- Retrieval endpoints reflect the results of the most recent successful compilation run.
- Accounting teams can export the schedule data for disclosure or audit purposes.
