# Lease Amortization Schedule Runtime Creation

## Context

Switching the `LeaseAmortizationSchedule.leaseContract` association to `@ManyToOne` means a single lease contract may own several amortization schedules. The legacy repository method `findAdjacentScheduleByBookingId` used a native query that fetched the first schedule matching a booking identifier. Once more than one schedule existed for the same booking id the query started returning multiple rows, triggering `IncorrectResultSizeDataAccessException` and breaking the batch compilation workflow that needs a schedule reference for every liability.

## Change

Instead of querying for a schedule by booking id, the amortization compilation service now creates a fresh `LeaseAmortizationSchedule` record at runtime for the liability being processed. The service sets the correct lease liability and IFRS16 lease contract references and persists the schedule through `InternalLeaseAmortizationScheduleService.save`. The generated schedule identifier is a random UUID so concurrent compilations do not clash.

## Impact

* Compilation jobs are no longer coupled to pre-existing schedules, so the process works even when multiple schedules share the same lease contract.
* Repository and service layers no longer expose the booking-id lookup, preventing future callers from reintroducing the incorrect query.
* Each generated batch of `LeaseLiabilityScheduleItem` entities is tied to a dedicated amortization schedule entity, simplifying traceability and cleanup.
