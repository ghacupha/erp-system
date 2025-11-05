# Lease Liability Schedule Metadata

The IFRS16 liability dashboard now uses a bespoke Angular component rather than
the generic report-summary view. `ReportMetadataSeederExtension` seeds the
navigation entry `lease-liability-schedule-report/report-nav`, pointing at the
same REST endpoint `api/leases/lease-liability-schedule-items` but only
exposing the `leaseContractId.equals` filter. The dashboard itself handles
period logic client-side, so no additional filter metadata is required. This
ensures the search console still lists the launch point after deployments while
preventing stale filters from constraining the schedule rows.

Recent schema updates introduced two server-side controls that the navigation
layer now relies on. Each schedule item carries an `active` flag that defaults
to `true`, allowing back office teams to temporarily suppress mis-staged rows
without deleting audit evidence. The REST resource exposes the field through
`active.equals` and `active.specified` filters, so downstream dashboards can
decide whether to render inactive lines or keep them out of their totals. The
entity also holds an optional link to `leaseLiabilityCompilationId`, giving
reporting batches a stable handle for the compilation request that produced the
item. When the compilation filter is supplied, the API returns only the rows
captured during that run, helping analysts reconcile regenerated schedules with
their originating approval trail.
