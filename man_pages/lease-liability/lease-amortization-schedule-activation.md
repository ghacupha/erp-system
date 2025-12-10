# Lease amortization schedule activation workflow

## Context
Lease amortization schedules now carry an `active` flag (default `true`) and expose activation/deactivation endpoints under `/api/leases/lease-amortization-schedules/{id}/activate|deactivate`. Front-end list views surface buttons mirroring the lease liability compilation controls.

## Behaviour
- **Activation**: posting to `/activate` flips the schedule to active and sets `lease_liability_schedule_item.active = true` for every item tied to the schedule. The REST call returns a `204` with an alert header containing the affected row count.
- **Deactivation**: posting to `/deactivate` marks the schedule inactive and sets all related schedule items to `active = false`.
- **Default state**: newly created schedules start with `active = true` unless explicitly overridden via the edit form checkbox.

## UI touchpoints
- The lease amortization schedule list now shows status badges and row-level Activate/Deactivate buttons with spinner/error feedback. The edit form includes a required Active checkbox, and the detail view displays the current status.

## Data updates
- Database schema adds `lease_amortization_schedule.active` (boolean, not null, default true) via Liquibase `20241118120000_added_active_to_LeaseAmortizationSchedule.xml`.
- Repository methods update both schedule and child items in a single request, preserving search index writes through the existing service layer.
