# Lease Settlement Reconciliation

## Purpose
Use this workflow to reconcile invoice settlements with scheduled lease payments for a specific repayment period. The reconciliation updates settlements with variance details and generates posting contexts for the rule engine.

## Steps
1. Open the lease settlement reconciliation area for the relevant repayment period.
2. Trigger reconciliation for the period.
3. Review the settlement entries:
   - **Reconciliation status** shows whether each settlement matched the schedule.
   - **Variance amount** and **variance reason** explain any differences.
   - **Posting ID** provides the audit reference for rule engine postings.

## Outcomes
- On-time, fully matched settlements are marked as **Reconciled**.
- Delayed, premature, over, under, or partial settlements are marked as **Variance** and flagged for posting.
