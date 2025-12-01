# Story: Navigate the ROU depreciation schedule dashboard

## Persona
- **Role:** Lease accountant comparing right-of-use amortisation across contracts.

## Steps
1. Opens the "ROU Depreciation Schedule" item under Lease Reports, which loads the navigation form at `/rou-depreciation-schedule-view/report-nav`.
2. Uses the IFRS16 lease contract search control to find the correct contract, submits the form, and lands on `/erp/rou-depreciation-schedule-view/{contractId}`.
3. Adjusts the **As at** date (defaulting to today) beside the contract dropdown to review the schedule up to that cut-off.
4. Confirms the initial ROU amount, total depreciation, closing NBV and **Current period depreciation** (year-to-date for the selected date) shown above the table for the chosen date.
5. Exports the table via **Export CSV** or **Export Excel** to share the filtered contract schedule with colleagues or auditors.
6. Scans the period rows to confirm each depreciation charge aligns with the lease period dates shown.
7. Picks another lease contract from the dropdown to repeat the check without leaving the page.

## Expected outcome
- The schedule table reloads for each selection, preserving the ordering by lease period and reflecting the backend depreciation results. The "Initial amount" column rolls forward from the previous period's outstanding balance after the first row.
- The summary figures and export files refresh immediately when the selected contract or as-at date changes, limiting the view to periods ending on or before the chosen date.
- The **Current period depreciation** card adds up depreciation for periods within the selected year up to the chosen date so accountants can state the year-to-date expense directly from the dashboard.
- Amounts populate even when the backend returns snake-case column names because the client normalises the payload before rendering.
