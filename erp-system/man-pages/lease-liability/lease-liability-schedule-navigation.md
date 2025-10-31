# Lease Liability Schedule Metadata

The IFRS16 liability dashboard relies on the dynamic report summary module. The
backend now seeds a `ReportMetadata` entry for
`reports/view/lease-liability-schedule-report` via
`ReportMetadataSeederExtension`. The seed maps the page path to the REST
endpoint `api/leases/lease-liability-schedule-items` and publishes two filters:

* `leaseContractId.equals` — surfaced as a lease contract typeahead. It aligns
  with the NgRx-stored contract id injected by
  `ReportSummaryViewComponent` when the dashboard opens.
* `leasePeriodId.equals` — exposed as a lease repayment period dropdown so the
  schedule rows can be scoped to a reporting window.

By baking this definition into the seed we avoid 404 responses when the client
requests metadata for the dashboard slug after a fresh deployment.
