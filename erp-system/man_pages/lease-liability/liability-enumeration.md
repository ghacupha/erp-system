# Liability enumeration pipeline for IFRS 16 present value calculations

## Purpose
The liability enumeration workflow converts uploaded lease payments into discounted present values that align with IFRS 16’s requirement to measure lease liabilities at the present value of future payments. It introduces two persistence layers—`LiabilityEnumeration` for run configuration and `PresentValueEnumeration` for per-payment discounting—before updating the canonical `LeaseAmortizationCalculation` record for the related IFRS 16 lease contract.

## Key components
- **LiabilityEnumeration** – stores the interest rate (kept as text and numeric), compounding granularity (monthly/quarterly/yearly), activation flag, and links to both the IFRS 16 lease contract and the selected lease payment upload. It serves as the batch trigger and is reusable for re-runs.
- **PresentValueEnumeration** – Kafka-backed rows that capture the payment sequence number, payment date/amount, per-period discount rate, and resulting present value. They provide a verifiable audit trail of each discounted cash flow.
- **LeaseAmortizationCalculation update** – the processor aggregates the enumerated present values to set `leaseAmount`, `numberOfPeriods`, `periodicity` (compounds per year: 12/4/1), and `interestRate` on the one-to-one amortization calculation tied to the contract.

## Processing flow
1. **Input validation**
   - The request supplies the lease contract ID, lease payment upload ID, annual interest rate (as a string to preserve precision), desired granularity, and optional active flag.
   - The upload must belong to the same contract; active payments are fetched in payment-date order.
2. **Computation rules**
   - The first payment month anchors the schedule. Period indices advance by 1/3/12-month steps for yearly/quarterly/monthly options.
   - The per-period rate is `annualRate / compoundsPerYear` and each present value is `payment / (1 + rate)^periodIndex`, rounded to two decimals after high-precision math.
3. **Asynchronous persistence**
   - Each computed line is published to the `present-value-enumeration` Kafka topic and persisted by the consumer as a `PresentValueEnumeration` row.
4. **Lease amortization roll-up**
   - The processor sums the present values and updates or creates the `LeaseAmortizationCalculation` for the contract. Repeated enumerations overwrite the same record, allowing schedule corrections without duplicating the amortization root.

## Batch execution and client visibility
- The long-running work now executes as a Spring Batch job (`liabilityEnumerationJob`) composed of scoped processors: input validation, contract/upload lookup (with creation of the `LiabilityEnumeration` row), queue dispatch for `PresentValueEnumeration`, and a writer that upserts the amortization calculation summary.
- Any failures in the batch bubble back through the REST call so the Angular list/update views display the errors via the shared alert service.
- New client pages under the ERP leases area expose a list of liability enumerations, a simple form to launch a new enumeration, and a detail grid for the present value lines tied to a run, making the background processing progress visible to lease managers.

## Operational notes
- The REST entry point is `POST /api/leases/liability-enumerations` with a JSON body matching `LiabilityEnumerationRequest`.
- Granularity is strict: values outside `MONTHLY`, `QUARTERLY`, or `YEARLY` are rejected.
- Failed lookups (contract/upload) or missing active payments halt the run with clear error messages so the CSV upload or contract wiring can be fixed before retrying.
