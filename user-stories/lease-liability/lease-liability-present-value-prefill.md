# Story: Prefill lease liability from present value enumeration

## Persona
Lease accountant preparing a new lease liability after uploading payments and running a liability enumeration.

## Flow
1. Open **Lease Liability** and choose the IFRS16 lease contract that was used for the latest liability enumeration.
2. Observe that **Lease Id** and dates still populate from the contract as before.
3. The form now auto-fills **Liability Amount** with the summed present values from the most recent enumeration for that contract.
4. The **Interest Rate** field auto-fills with the rate used by that enumeration.
5. Save the liability to persist the prefilled amount and rate.

## Expected outcome
- Amount and rate reflect the latest enumerated present value schedule without manual re-entry.
- If no enumeration exists for the contract, the amount and rate remain untouched so the accountant can enter them manually.
