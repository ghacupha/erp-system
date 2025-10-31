# Lease Liability Dashboard – User Story

## Persona
Lease Manager responsible for IFRS16 reporting.

## Narrative
1. The Lease Manager opens the ERP leases workspace and navigates to `#/lease-liability-schedule-view/{contractId}` for the contract under review.
2. The dashboard renders the contract title, liability identifier, and headline metrics for the most recent repayment period.
3. The manager inspects the monthly schedule rows, noting the days since the previous payment and the split between cash, principal, and interest.
4. The manager selects an earlier period from the dropdown to compare balances; the table and totals refresh immediately without a full page reload.
5. If a period has no amortisation rows the dashboard communicates the empty state while still exposing the contract context.

## Acceptance Criteria
- The feature requires `ROLE_LEASE_MANAGER` permissions and only shows data for the selected contract.
- Numbers and dates respect the shared formatting pipes so the dashboard aligns with other lease reports.
- Period switching recalculates totals and row content client-side; no manual refresh is needed.
