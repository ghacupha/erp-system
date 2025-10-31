# UI Story: Lease liability schedule dashboard launch

* The Lease Reports menu exposes a "Liability Schedule Dashboard" entry for
  users with `ROLE_LEASE_MANAGER`.
* The entry routes to `/lease-liability-schedule-report/report-nav`, presenting
  the IFRS16 lease contract selector implemented by
  `Ifrs16LeaseReportNavComponent`.
* Submitting the form dispatches the NgRx selection action and opens
  `/lease-liability-schedule-view/{id}`, which renders the bespoke
  dashboard for the selected contract.
* Attempting to access the dashboard without a stored contract selection causes
  an automatic redirect back to the parameter form.
