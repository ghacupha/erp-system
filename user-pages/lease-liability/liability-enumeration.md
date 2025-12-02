# How to enumerate lease liability present values

Use this guide to run the IFRS 16 present value calculation from uploaded lease payments and refresh the lease amortization calculation.

## Prerequisites
- An IFRS 16 lease contract has been created (Booking ID visible).
- The lease payment CSV is uploaded and active for the same contract.

## Steps
1. Go to **Processing → Lease Amortization Calculation → Enumerate Liability**.
2. Provide the inputs:
   - **Lease Contract** – choose the correct booking ID.
   - **Lease Payment Upload** – pick the uploaded CSV batch for this contract.
   - **Interest Rate** – enter the annual rate as a precise decimal (e.g., `0.0875`).
   - **Time Granularity** – select **Monthly**, **Quarterly**, or **Yearly** to match the desired compounding period.
3. Submit the form. The system validates the upload/contract match and ensures active payments exist.
4. The processor discounts each payment, writes the results to the Kafka-backed present value queue, and updates the lease amortization calculation with:
   - Total present value as the lease amount
   - Periodicity code (12, 4, or 1) based on granularity
   - Number of discounted periods and the chosen interest rate
5. To adjust a schedule, fix the payments or rate and rerun the enumeration; the existing amortization calculation for the contract is updated in place.

## Expected results
- Errors are reported immediately for missing uploads, contract mismatches, or inactive payment sets.
- Discounted payments appear in audit views once the Kafka consumer persists them.
- Lease liabilities and downstream forms (e.g., LeaseLiability or ROU pages) pull the refreshed lease amount and compounding settings from the updated amortization calculation.
