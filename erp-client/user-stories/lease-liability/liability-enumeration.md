# User story: Run liability enumeration with searchable inputs

## Persona
- **Role:** Lease accountant preparing IFRS16 liability schedules

## Goals
- Select the correct lease contract and payment upload without memorising IDs.
- Ensure the enumeration uses monthly discounting even for quarterly or annual settlements.

## Workflow
1. Open the *Liability enumeration* create page.
2. Use the **Lease contract** NgSelect to search by title/booking ID and choose the contract.
3. Use the **Lease payment upload** NgSelect to pull the upload linked to that contract and select it.
4. Enter the annual interest rate and confirm the payment frequency (monthly, quarterly, bi-annual or yearly).
5. Submit; the system launches enumeration with monthly discounting and zero-value months between non-monthly payments.

## Acceptance criteria
- Selecting a lease contract updates the form and filters payment uploads.
- Choosing a payment upload fills the upload ID and keeps the matching contract.
- The form prevents submission until both selections and the interest rate are provided.
- The resulting schedule discounts monthly with zero cashflows in months without settlements.
