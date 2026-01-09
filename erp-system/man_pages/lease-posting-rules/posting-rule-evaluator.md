# Lease Posting Rule Evaluator (Technical Notes)

## Overview
The lease posting workflow previously relied on TA-specific rule entities (for example `TALeaseRepaymentRule`, `TALeaseInterestAccrualRule`, and related repositories) to resolve debit/credit accounts during posting. The `TransactionAccountPostingRule` entity existed with a standard CRUD resource, query service, and repository, but it was not referenced by the lease posting services (only the REST CRUD endpoints and query criteria used it). This meant the rule definition was effectively unused in production posting paths.

This change consolidates lease posting into a rule-engine flow that resolves posting templates via `TransactionAccountPostingRule`. A new `PostingRuleEvaluator` service accepts a posting context (module/event and contextual attributes) and emits balanced `TransactionDetails` records.

## Key Design Decisions
- **Scenario metadata on rules**: `TransactionAccountPostingRule` now carries scenario fields (module, event type, variance type, invoice timing) so rules can be scoped by module/event and further filtered by scenario-specific attributes.
- **Structured conditions and templates**: two linked entities capture rule constraints and posting templates:
  - `TransactionAccountPostingRuleCondition` stores key/operator/value predicates that are matched against the posting context.
  - `TransactionAccountPostingRuleTemplate` stores debit/credit accounts and optional amount multipliers/line descriptions.
- **Rule evaluation**: the evaluator selects rules by module/event, checks scenario fields and conditions, and then instantiates `TransactionDetails` per template. Each template produces a balanced debit/credit entry and inherits transaction metadata from the posting context.

## Lease Posting Integration
The lease posting service now builds a `PostingContext` for each lease event (repayments, interest accrual, interest paid transfer, lease liability recognition, ROU recognition, and ROU amortization). These contexts include:
- The module/event identifiers (e.g., `LEASE` + `LEASE_REPAYMENT`).
- The amount, transaction date, description, posting ID, and user.
- Attributes such as `leaseContractId`, `leasePeriodId`, or ROU entry identifiers for rule conditions to match.

The service then delegates rule resolution to the evaluator, which removes direct dependency on the TA rule entities.

## Follow-up Considerations
- If rule conditions are needed for additional lease-specific criteria (e.g., variance type), add key/value entries to the rule conditions instead of hardcoding logic in the lease posting service.
- The rule engine relies on configured templates. Missing templates or unmatched rules now surface as explicit errors during posting, highlighting configuration gaps.
