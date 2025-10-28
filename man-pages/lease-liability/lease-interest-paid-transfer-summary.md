# Lease Interest Paid Transfer Summary Endpoint

This report exposes the lease interest paid transfer totals for a given repayment
period. It highlights which dealer and accounts are involved when the cash
interest is transferred and omits rows whose summed interest payment equals zero
for clarity.

* **Endpoint**: `GET /api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}`
* **Source Query**: `InternalLeaseLiabilityScheduleReportItemRepository#getLeaseInterestPaidTransferSummary`
* **Projection**: `LeaseInterestPaidTransferSummaryInternal` mapped to
  `LeaseInterestPaidTransferSummaryDTO`
* **Joins**: `lease_liability_schedule_item`, `ifrs16lease_contract`, `dealer`,
  `tainterest_paid_transfer_rule`, `transaction_account`
* **Aggregation**: `SUM(COALESCE(llsi.interest_payment,0))` grouped per lease
  contract and filtered with a `HAVING` clause to exclude zero totals.

See the detailed notes inside `erp-system/man-pages/lease-liability/`
for developer-oriented context and testing instructions.
