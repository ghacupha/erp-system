# Lease Settlement Reconciliation User Story

## Persona
A lease accounting analyst responsible for verifying invoice settlements against the lease payment schedule.

## Scenario: Reconcile lease settlements for a period
1. The analyst navigates to the lease settlement reconciliation screen for a specific repayment period.
2. They trigger reconciliation for the selected period.
3. The system evaluates each settlement against the scheduled payment rows and records the reconciliation outcomes.
4. The analyst reviews the generated reconciliation results, including any variance reasons and posting references.

## Expected Outcome
- Lease settlements are updated with reconciliation status, variance amount, variance reason, and posting identifiers.
- Variances such as delayed, premature, over, under, or partial settlements are flagged for posting by the rule engine.
