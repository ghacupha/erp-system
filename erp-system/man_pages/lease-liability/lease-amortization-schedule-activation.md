# Lease amortization schedule activation workflow

## Context
The lease amortization schedule entity now includes an `active` flag (defaulting to `true`) and dedicated activation endpoints at `/api/leases/lease-amortization-schedules/{id}/activate` and `/deactivate`. These mirror the existing compilation controls but target schedule-linked items.

## Behaviour
- **Activate**: marks the schedule active and updates every related `lease_liability_schedule_item` row to `active = true`. The REST response uses a `204` with an alert header carrying the affected count.
- **Deactivate**: flips the schedule and all associated schedule items to inactive via the same service method.
- **Defaults**: the edit form presents a required Active checkbox; new schedules remain active unless the checkbox is cleared.

## UI and data
- The list view now displays status badges and Activate/Deactivate buttons per row with loading/error feedback.
- Liquibase change set `20241118120000_added_active_to_LeaseAmortizationSchedule.xml` adds the `active` column to the table with a non-null, default `true` constraint.
- The internal schedule service delegates activation toggles to the schedule-item service, ensuring both parent and children stay in sync.
