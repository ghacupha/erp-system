# Lease Settlement Reconciliation Workflow

## Overview
The lease settlement reconciliation workflow aligns invoice settlements with the scheduled lease payments for a given repayment period. It supports auditability by capturing reconciliation status, variance amounts, variance reasons, and posting identifiers in the `LeaseSettlement` entity. This record is then used to emit posting contexts for the rule engine to drive downstream postings for timing and amount variances.

## Business Purpose
Lease settlement data typically comes from invoices (actual settlements), while the lease payment schedule represents expected cash flows. Reconciling these ensures that accounting postings capture variances such as delayed settlements, premature settlements, and partial or over/under payments. The reconciliation output provides a structured context that the transaction posting rules can evaluate.

## Key Components
- **`LeaseSettlement` entity**: Stores settlement date, invoice amount/reference, variance amount, reconciliation status, posting ID, and variance reason, plus links to the lease contract, period, and optional scheduled payment.
- **`LeaseSettlementReconciliationServiceExtension`**: Compares settlements to scheduled payments for a given repayment period and derives variance classifications.
- **Posting context emission**: When variances are detected, posting contexts are built with the `LEASE_SETTLEMENT_RECONCILIATION` event type and variance metadata (timing and amount).
- **REST endpoint**: The reconciliation is triggered per period through `/api/leases/lease-settlements/reconcile/{periodId}`.

## Workflow Summary
1. **Load period context**: The reconciliation service retrieves the selected `LeaseRepaymentPeriod` to obtain its date range.
2. **Fetch schedule and settlements**: Scheduled `LeasePayment` rows in the period are grouped by lease contract, and `LeaseSettlement` rows for the same period are loaded.
3. **Determine expected payment**: Each settlement is paired with its linked `LeasePayment` when present; otherwise, the earliest scheduled payment for the same contract is used.
4. **Compute variances**:
   - **Timing**: `PREMATURE`, `ON_TIME`, or `DELAYED` by comparing settlement date against the expected date.
   - **Amount**: `OVER`, `PARTIAL`, `UNDER`, or `MATCHED` by comparing invoice amount to scheduled amount.
5. **Persist audit fields**: Variance amount, variance reason, reconciliation status, and posting ID are saved on the settlement.
6. **Emit posting contexts**: For variance cases, posting contexts include the variance type, timing, amount, period, and identifiers. These can be consumed by the posting rule engine.

## Design Decisions
- **Variance audit fields on `LeaseSettlement`**: Storing variance metadata on the settlement keeps audit trails close to the transactional record.
- **Context emission via extension service**: Custom reconciliation logic lives in an extension service to avoid direct modifications to generated service classes.
- **Per-period trigger**: Reconciliation is designed to run for a single repayment period, making it deterministic and supporting incremental processing.

## Notes
- Variance reasons combine timing and amount classifications to make the reconciliation outcome human-readable.
- When settlements match the schedule with on-time timing, the reconciliation status is `RECONCILED` and no posting context is emitted.
