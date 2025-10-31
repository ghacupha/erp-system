# Lease Liability Schedule Metadata

The IFRS16 liability dashboard now uses a bespoke Angular component rather than
the generic report-summary view. `ReportMetadataSeederExtension` seeds the
navigation entry `lease-liability-schedule-report/report-nav`, pointing at the
same REST endpoint `api/leases/lease-liability-schedule-items` but only
exposing the `leaseContractId.equals` filter. The dashboard itself handles
period logic client-side, so no additional filter metadata is required. This
ensures the search console still lists the launch point after deployments while
preventing stale filters from constraining the schedule rows.
