# Lease Liability Outstanding Summary

The lease liability outstanding summary consolidates the closing balances that remain on each IFRS16 lease contract for a
selected repayment period. The projection aggregates the **outstanding balance** and **interest payable closing** columns of
`lease_liability_schedule_item` so that finance teams can reconcile the liability and accrued interest control accounts that are
presented in statutory reporting packs.

## Data sources

* `lease_liability_schedule_item` – filters rows by the supplied `lease_period_id` and provides `outstanding_balance` and
  `interest_payable_closing` totals.
* `ifrs16lease_contract` – exposes the booking identifier and dealer reference for the lease so that the UI can show user-friendly
  context around each aggregated balance.
* `talease_recognition_rule` – contributes the liability GL account via its `credit_id` reference to `transaction_account`.
* `talease_interest_accrual_rule` – contributes the interest payable GL account via its `credit_id` reference to
  `transaction_account`.

## API contract

The REST endpoint is exposed on `/api/leases/lease-liability-schedule-report-items/liability-outstanding-summary/{leasePeriodId}`
and returns an array of rows that contain the following fields:

| Field | Description |
| --- | --- |
| `leaseId` | Booking identifier of the contract driving the aggregation. |
| `dealerName` | Dealer tied to the IFRS16 contract. |
| `liabilityAccount` | Control account sourced from the lease recognition rule credit leg. |
| `interestPayableAccount` | Accrued interest account sourced from the interest accrual rule credit leg. |
| `leasePrincipal` | Sum of `outstanding_balance` for the contract within the chosen period. |
| `interestPayable` | Sum of `interest_payable_closing` for the contract within the chosen period. |

Zero-only rows are excluded so that downstream exports focus on active balances.
