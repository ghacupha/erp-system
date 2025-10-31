# Story: Lease Manager launches the lease liability schedule dashboard

*As a* lease manager

*I want* to choose the IFRS16 lease contract I am interested in before the
liability schedule dashboard loads

*So that* the dashboard opens with the contract context applied and I am not
presented with an empty report.

## Acceptance Criteria

1. Launching "Liability Schedule Dashboard" from the Lease Reports menu opens a
   parameter page that contains a single IFRS16 lease contract typeahead field.
2. Selecting a contract and clicking **Open Dashboard** routes to
   `/lease-liability-schedule-view/{contractId}` and displays the dedicated
   dashboard component.
3. Navigating directly to the dashboard without a stored contract id sends the
   user back to the parameter page.
4. Leaving the dashboard clears the stored contract selection so the next visit
   starts fresh.
