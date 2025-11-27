# Story: Browse the ROU depreciation schedule per lease contract

## Persona
- **Role:** Financial controller responsible for lease accounting disclosures.

## Steps
1. Opens the ERP web app and selects **ROU Depreciation Schedule** under Lease Reports to load the navigation form.
2. Searches for the IFRS16 lease contract in the M21 selector, submits the form and lands on the depreciation schedule dashboard for that contract.
3. Adjusts the **As at** date (defaulting to today) beside the contract dropdown to focus on the schedule up to that cut-off.
4. Confirms the headline figures (initial ROU amount, total depreciation, closing NBV) and scrolls through the period-by-period rows for that date.
5. Downloads the table via **Export CSV** or **Export Excel** to share the filtered contract schedule with auditors.
6. Switches to a different lease contract using the dropdown on the dashboard to compare schedules without leaving the view.

## Expected outcome
- The schedule table shows the lease periods in order with depreciation and outstanding NBV values that match the backend calculation for the chosen contract, and the "Initial amount" column rolls forward from the previous period's outstanding balance after the first row.
- The totals and export files update automatically when the controller changes the selected contract or the as-at date, reflecting only periods ending on or before the chosen cut-off.
- Figures remain visible even if the backend responds with snake-case column names because the client normalises the payload before rendering.
