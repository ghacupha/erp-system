# User story: Toggle lease amortization schedule status

## Persona
Lease accountant responsible for preparing and validating lease liability amortization schedules.

## Narrative
- The accountant uploads a CSV amortization schedule or triggers an automated compilation for a lease liability.
- The system creates a lease amortization schedule that is **active** by default and links it to the originating lease liability compilation.
- When the accountant clicks **Deactivate** on a schedule row, the system marks the schedule, all compiled schedule items, and the parent compilation as inactive so downstream reports ignore the run.
- When the accountant clicks **Activate**, the same three layers flip back to active and the compilation status reflects the promotion of that schedule.

## Acceptance criteria
- Newly created schedules (via upload or compilation) show as active without manual edits.
- Activating or deactivating a schedule updates the compilation's status and the related schedule items in one step.
- Refreshing the schedules list always shows the persisted activation state with no transient mismatches.
