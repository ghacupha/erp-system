# User story: toggle lease amortization schedules

- **Persona:** Lease manager
- **Objective:** Control whether a lease amortization schedule and its related liability schedule items participate in processing.

## Steps
1. Navigate to **Leases → Lease Amortization Schedules**.
2. Review the status badge in the Status column for the target schedule.
3. Click **Activate** to switch a schedule back on, or **Deactivate** to pause it.
4. Wait for the spinner to clear; the badge updates to reflect the new state.
5. All associated `lease_liability_schedule_item` rows inherit the same active flag automatically.

## Outcomes
- Active schedules push their child schedule items to `active = true`.
- Deactivated schedules push their child schedule items to `active = false`, preventing further downstream runs until reactivated.
- Row-level errors are shown inline if an action fails, prompting the user to retry after resolving the cause.
