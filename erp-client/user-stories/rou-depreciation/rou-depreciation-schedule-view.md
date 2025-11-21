# Story: Navigate the ROU depreciation schedule dashboard

## Persona
- **Role:** Lease accountant comparing right-of-use amortisation across contracts.

## Steps
1. Starts typing "ROU depreciation schedule" into the report search bar and chooses the suggested dashboard.
2. Arrives at `/erp/rou-depreciation-schedule-view` and selects a lease contract from the dropdown.
3. Confirms the initial ROU amount, total depreciation and closing NBV shown above the table.
4. Scans the period rows to confirm each depreciation charge aligns with the lease period dates.
5. Picks another lease contract from the dropdown to repeat the check without leaving the page.

## Expected outcome
- The schedule table reloads for each selection, preserving the ordering by lease period and reflecting the backend depreciation results.
- The summary figures refresh immediately when the selected contract changes.
