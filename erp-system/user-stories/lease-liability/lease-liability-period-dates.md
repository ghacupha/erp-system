# Story: View repayment period dates on lease liability schedule

## Persona
Lease accounting analyst reviewing monthly liability schedules.

## Goal
Ensure each schedule item clearly shows the configured start and end dates for its repayment period.

## Steps
1. Open the Lease Liability module and navigate to the liability schedule for a lease.
2. Inspect the Start and End columns in the schedule table.
3. Confirm that each row displays the repayment period's start and end dates matching the configuration in Lease Repayment Periods.
4. Toggle the **Active** filter to hide inactive schedule items and verify that dormant rows disappear from the listing while active rows remain unchanged.
5. If reconciling a compilation run, apply the **Compilation Request** filter so that only schedule items generated during that batch remain visible.

## Expected Result
The Start and End columns contain the accurate period dates instead of remaining blank or showing placeholder values. The period code remains visible alongside the dates for cross-reference. Applying the Active filter removes inactive rows without disturbing historical data, and selecting a compilation narrows the view to the expected batch of schedule items.
