# User Story: Migrate Legacy Lease Posting Rules

## Persona
**Finance Administrator** responsible for keeping lease posting configurations current.

## Scenario
The administrator needs to replace legacy lease posting rule screens with posting-rule engine configuration for all lease events.

## Steps
1. Review existing legacy rule configurations for lease repayment, interest accrual, interest paid transfer, lease liability recognition, ROU recognition, and ROU amortization.
2. Open the posting rule administration page.
3. Create one posting rule per lease event, using `LEASE` as the module and the matching event type.
4. Add posting templates with the debit and credit accounts that match the legacy configurations.
5. Add conditions (for example, `leaseContractId`) when rules should apply only to specific leases.
6. Save the new rules and disable or retire the legacy rule screens in day-to-day use.

## Expected Outcome
All lease postings use the posting rule engine, and legacy rule screens are no longer required to produce correct `TransactionDetails` entries.
