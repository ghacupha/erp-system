# User Story: Sync Lease Posting Rule Categories

## Persona
**Lease Accounting Administrator** who maintains posting rules and ensures the account categories match the configured debit and credit accounts.

## Scenario
Lease templates are updated for existing contracts, but the posting rule header categories are out of sync. The administrator needs to align the posting rule header and templates with the TA rule configuration so that postings continue to use the correct categories.

### Steps
1. Review the lease contract and confirm the TA rules (interest accrual, interest paid transfer, repayment, recognition) are correct.
2. Ensure each lease posting rule has a `leaseContractId` condition that targets the lease contract.
3. Run the lease posting rule category sync SQL script.
4. Refresh the lease posting rule configuration list.

## Expected outcome
The posting rule templates use the correct debit/credit transaction accounts, and the posting rule header debit/credit categories match the account categories of those accounts.
