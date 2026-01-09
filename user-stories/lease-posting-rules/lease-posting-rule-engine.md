# User Story: Configure Lease Posting Rules

## Persona
**Lease Accountant** responsible for ensuring lease postings use the correct debit/credit accounts for each lease event.

## Scenario
The Lease Accountant needs to configure posting rules for lease repayment and interest accrual so that postings are routed through the rule engine instead of TA-specific rules.

### Steps
1. Navigate to the posting rule administration screen.
2. Create a new posting rule for the module `LEASE` and event `LEASE_REPAYMENT`.
3. Add conditions such as `leaseContractId` (if the rule should be lease-specific) and verify the operator/value.
4. Add one or more posting templates with debit/credit accounts and any amount multipliers.
5. Repeat the process for `LEASE_INTEREST_ACCRUAL` and other lease events as required.
6. Save and validate the rules.

## Expected Outcome
When the Lease Accountant triggers lease posting, the system evaluates the configured rule conditions and generates balanced `TransactionDetails` entries using the posting templates.
