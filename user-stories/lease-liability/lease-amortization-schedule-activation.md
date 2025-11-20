# User story: toggle lease amortization schedules

- **Persona:** Lease manager maintaining IFRS16 schedules.
- **Goal:** Switch a lease amortization schedule and its liability schedule items on or off when moving between modelling and production runs.
- **Preconditions:** The lease amortization schedule exists and the user holds lease manager permissions.

## Happy path
1. Open *Leases → Lease Amortization Schedules*.
2. Locate the target schedule and review the status badge in the Status column.
3. Click **Activate** to re-enable a paused schedule.
4. The row shows a spinner until the backend confirms the change, then the status badge flips to *Active*.
5. All related liability schedule items are now marked active.

## Alternate path – deactivation
1. From the same list, click **Deactivate** on an active schedule.
2. The row shows progress, then updates to *Inactive*.
3. All related liability schedule items become inactive, preventing downstream processing.

## Error handling
- If the backend rejects the request, the row shows an inline error message so the user can retry after resolving the issue.
