# User story: Enumerate lease liability present values

## Persona
- **Lease accountant** preparing IFRS 16 onboarding or schedule corrections.

## Goal
Generate an auditable set of discounted lease payments and refresh the lease amortization calculation without manually rekeying the payment schedule.

## Steps
1. Navigate to **Processing → IFRS 16 Lease Payments** and upload the CSV for the contract.
2. Open **Processing → Lease Amortization Calculation → Enumerate Liability** and submit:
   - Lease contract (booking ID shown)
   - Lease payment upload reference
   - Annual discount rate (as a precise decimal string)
   - Time granularity (Monthly/Quarterly/Yearly)
3. The system validates that the upload belongs to the contract and that it has active payments.
4. The processor discounts each payment using the selected granularity, publishes the results to the Kafka queue, and updates the lease amortization calculation with the summed present value, period count, periodicity code (12/4/1), and interest rate.
5. Re-run the enumeration after adjusting payments or rate inputs to overwrite the same amortization record for the contract.
6. Review the **Liability Enumerations** list to confirm the run record and drill into the **Present Values** grid for the enumerated cash flows. Any batch failure pops an alert in either the list or the launch form so the accountant can retry with corrected inputs.

## Acceptance criteria
- Invalid contract/upload combinations or missing payments return clear validation errors with no partial updates.
- Present value rows appear in downstream analytics once the Kafka consumer persists them.
- Lease amortization calculations reflect the latest enumeration totals and compounding frequency.
