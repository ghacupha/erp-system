# ROU depreciation schedule view endpoint

## Context

The lease liability module already exposes a schedule view that lines up repayment periods with the corresponding principal and interest movements. The depreciation workflow for the right-of-use (ROU) asset needed an equivalent view so analysts can track how the net book value unwinds over each lease period.

## Data sources

- `rou_depreciation_entry`: stores the amortisation figures produced by the ROU depreciation batch. Each row contains the depreciation charge and the updated outstanding amount for a lease period.
- `lease_period`: provides the chronological ordering of ROU periods (different from the lease repayment period used for the liability view).
- `rou_model_metadata`: carries the initial ROU asset amount that anchors the schedule.

## API surface

A new production resource exposes the schedule in a read-only projection:

- **Route:** `GET /api/leases/rou-depreciation-schedule-view/{leaseContractId}`
- **Payload:** ordered list of rows with the period code, start/end dates, sequence number, initial ROU amount, depreciation charge and the outstanding net book value.
- **Ordering:** lease periods are sorted by start date, then by the stored sequence number to mirror the depreciation run.

## Implementation notes

- `InternalRouDepreciationEntryRepository` now publishes a native query that joins depreciation entries to their lease period, lease contract and ROU metadata. The query aliases columns so they hydrate the `RouDepreciationScheduleViewInternal` projection.
- `RouDepreciationScheduleViewServiceImpl` wraps the repository call to keep scheduling logic out of the controller.
- `RouDepreciationScheduleViewResourceProd` provides the REST entry point and logs the requested lease contract identifier for traceability.
- The service now reapplies the running balance logic before returning the projection so that every row after the first reports the previous period's outstanding balance as its initial amount, matching the ledger chain shown in the depreciation entries.

### Front-end availability

- The ROU depreciation schedule is exposed to the report search index through a `ReportMetadataSeed` with the page path `erp/rou-depreciation-schedule-view` and the backing API above, making it discoverable from the navbar search.
- A dedicated Angular view (`RouDepreciationScheduleViewComponent`) lets users pick an IFRS16 lease contract and browse its depreciation steps. The view surfaces the initial amount, cumulative depreciation and closing net book value alongside the period-by-period rows that the API returns.

The schedule view mirrors the liability schedule UX while aligning with the ROU data model, ensuring the depreciation run can be audited period by period until the asset is fully amortised.
