# Lease Interest Paid Transfer Summary Endpoint

## Overview

The lease interest paid transfer summary endpoint aggregates interest payment rows
for a specific repayment period and exposes the debit and credit accounts used when
booking the transfer. It complements the existing interest expense summary by
focusing on cash settlements rather than accruals.

* **Endpoint**: `GET /api/leases/lease-liability-schedule-report-items/interest-paid-transfer-summary/{leasePeriodId}`
* **Purpose**: Provide a condensed view of the cash interest settled per lease
  contract, including the dealer name and debit/credit accounts configured on the
  transfer rule.
* **Inputs**: `leasePeriodId` – identifier of the repayment period targeted.
* **Outputs**: Array of rows with lease identifier, dealer name, narration,
  credit account number, debit account number, and the aggregated interest
  amount.

## Implementation Notes

1. A new projection `LeaseInterestPaidTransferSummaryInternal` models the SQL result
   set and is mapped to `LeaseInterestPaidTransferSummaryDTO` by
   `LeaseInterestPaidTransferSummaryInternalMapper`.
2. `InternalLeaseLiabilityScheduleReportItemRepository` exposes the native query
   `getLeaseInterestPaidTransferSummary`. The query joins:
   * `lease_liability_schedule_item` filtered by the supplied repayment period.
   * `ifrs16lease_contract` and `dealer` to expose the booking identifier and
     dealer name.
   * `tainterest_paid_transfer_rule` and related `transaction_account` rows to
     fetch debit and credit account numbers.
   It groups by lease contract and omits rows whose summed `interest_payment`
   equals zero.
3. `InternalLeaseLiabilityScheduleReportItemService` delegates to the repository
   and maps the projection to DTOs, performing an additional safeguard filter
   against zero-interest values.
4. `LeaseLiabilityScheduleReportItemResourceProd` exposes the new GET endpoint
   that returns the mapped DTO list.
5. Regression coverage lives in
   `LeaseLiabilityScheduleReportItemResourceProdIT`, where mocked repository
   results confirm that zero-interest rows are excluded and account numbers are
   present in the response payload.

## Testing

Run the regression test:

```bash
./mvnw -pl erp-system -am test -Dtest=io.github.erp.erp.resources.leases.LeaseLiabilityScheduleReportItemResourceProdIT
```

The test verifies the controller wiring, mapping, and filtering behaviour using
MockMvc and Mockito stubs.
