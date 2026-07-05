# ROU depreciation schedule dashboard (client)

1. Open **Lease Reports → ROU Depreciation Schedule** to load the navigation form at `/rou-depreciation-schedule-view/report-nav`.
2. Search for the IFRS16 lease contract in the M21 selector, submit the form, and land on `/erp/rou-depreciation-schedule-view/{contractId}`.
3. Confirm the summary cards showing the initial ROU amount, cumulative depreciation, closing net book value and **Current period depreciation** (year-to-date for the chosen cut-off).
4. Use the **As at** date picker (defaulting to today) beside the contract dropdown to limit the schedule to periods ending on or before the selected date.
5. Review the table of lease periods with start/end dates, depreciation amounts and outstanding balance for that cut-off.
6. Download the current contract's schedule via **Export CSV** or **Export Excel** to share with colleagues.
7. Switch to another contract using the dropdown to compare results without leaving the view.

If the page shows an error banner, reselect the contract or retry once the backend schedule API is available. If the table loads but amounts look blank, refresh the page—the client now normalises snake-case responses so the figures should render after reload. The initial amount column rolls forward from the previous period's outstanding balance after the first row so the running NBV chain is visible without manual calculation.
