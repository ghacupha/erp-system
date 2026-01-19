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

## PostingRuleEvaluator Steps and Roles
1. **Validate context**: required fields (`module`, `eventType`, `amount`, `transactionDate`, `postedBy`) must be present.
2. **Load rule candidates**: fetch rules for the module/event pair in deterministic order.
3. **Match scenario + conditions**:
   - Optional scenario fields (`varianceType`, `invoiceTiming`, `transactionContext`) must match if set.
   - Each condition key/operator/value is matched against `PostingContext.attributes`.
4. **Select exactly one rule**: zero or multiple matches trigger explicit errors to surface configuration gaps.
5. **Expand templates**: each `TransactionAccountPostingRuleTemplate` generates a `TransactionDetails` row with debit/credit accounts and optional amount multiplier/line description.

### PostingContext Data Roles
- **Transaction metadata**: `transactionType`, `transactionDate`, `description`, and `postingId` define how the details appear on ledgers.
- **Scenario metadata**: `module`/`eventType` route to the correct rule set.
- **Condition payload**: `attributes` carry identifiers like `leaseContractId`, `leasePeriodId`, `leaseLiabilityId`, `rouInitialDirectCostId`, and `rouDepreciationEntryId`.
- **User/ownership**: `postedBy` is used on `TransactionDetails` and supports audit trails.
- **Placeholders**: optional placeholders provide transaction context matching for rules that require a transaction context.

### Posting Template Data Roles
`TransactionAccountPostingRuleTemplate` defines the line-level debit/credit pairing and optional multipliers:
- **Accounts**: `debitAccount` and `creditAccount` are the authoritative posting targets.
- **Amount behavior**: `amountMultiplier` scales the context amount when needed.
- **Line description**: optional per-line override; falls back to the `PostingContext` description.

## Replacing Legacy Lease Rule Entities
The legacy TA rule entities are now represented as posting-rule configurations with a single evaluator:
- `TALeaseRepaymentRule` -> `TransactionAccountPostingRule` with `module=LEASE`, `eventType=LEASE_REPAYMENT`.
- `TALeaseInterestAccrualRule` -> `module=LEASE`, `eventType=LEASE_INTEREST_ACCRUAL`.
- `TAInterestPaidTransferRule` -> `module=LEASE`, `eventType=LEASE_INTEREST_PAID_TRANSFER`.
- `TALeaseRecognitionRule` -> `module=LEASE`, `eventType=LEASE_LIABILITY_RECOGNITION`.
- `RouRecognitionRule` / initial direct cost -> `module=LEASE`, `eventType=LEASE_ROU_RECOGNITION`.
- `RouAmortizationRule` -> `module=LEASE`, `eventType=LEASE_ROU_AMORTIZATION`.

Each of these is now configured through templates and optional conditions instead of hardcoded repository lookups.

## Adjusted Compilation Request for Lease Posting
The posting compilation step (building `TransactionDetails`) now flows through `PostingContext` and templates:
- **Lease liability postings** use `leaseLiabilityId` and `leaseContractId` attributes for rule conditions, with `LEASE_LIABILITY_RECOGNITION` event routing.
- **ROU model metadata** is carried via `rouInitialDirectCostId` (recognition) and `rouDepreciationEntryId` (amortization), enabling rules to filter on ROU-specific entries when required.
- **Schedule-based postings** include `leasePeriodId` and fiscal-month-based descriptions, keeping schedule context without embedding account rules in the service.

## Follow-up Considerations
- If rule conditions are needed for additional lease-specific criteria (e.g., variance type), add key/value entries to the rule conditions instead of hardcoding logic in the lease posting service.
- The rule engine relies on configured templates. Missing templates or unmatched rules now surface as explicit errors during posting, highlighting configuration gaps.
