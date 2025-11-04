# Lease Liability Schedule Period Dates Serialization

## Summary
- ensure `LeaseRepaymentPeriodMapper#toDtoPeriodCode` includes `startDate`, `endDate`, and `sequenceNumber`
- expose repayment period dates in `api/leases/lease-liability-schedule-items`
- verify build; note Maven wrapper download blocked by offline environment

## Context
The lease liability schedule view consumes `LeaseLiabilityScheduleItemDTO` objects that include a nested `leasePeriod` summary. The front-end expects both the identifying period code and the configured start/end dates to populate the Start and End columns. Previously, the mapper ignored these date fields when creating the lightweight DTO, so the API omitted them.

## Change Details
1. Updated `LeaseRepaymentPeriodMapper#toDtoPeriodCode` to copy `startDate`, `endDate`, and `sequenceNumber` in addition to the identifier and period code. This ensures MapStruct serializes the dates for the nested projection used by the schedule item endpoint.
2. Triggered a server rebuild via `./mvnw test -DskipITs`. The wrapper attempted to download Maven but failed because outbound network access is disabled in the execution environment. The build therefore could not complete automatically.

## Manual Verification
- Full UI verification was not possible within the headless environment, but the API now provides the required fields. Local testers should reload the lease liability schedule view in the ERP client and confirm the Start/End columns display the configured repayment period dates for each row.

## Follow-Up
- Once network access is available, rerun `./mvnw test -DskipITs` (or the appropriate module test suite) to confirm the build passes end-to-end.
