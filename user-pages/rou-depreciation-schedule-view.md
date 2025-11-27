# ROU depreciation schedule dashboard

Use this page to review the period-by-period depreciation of a right-of-use asset.

## Steps
1. In **Lease Reports**, click **ROU Depreciation Schedule** to open the navigation form at `/rou-depreciation-schedule-view/report-nav`.
2. Search for and select the IFRS16 lease contract in the M21 control, then submit to navigate to `/erp/rou-depreciation-schedule-view/{contractId}`.
3. Review the summary cards for the initial ROU amount, total depreciation to date and the closing net book value.
4. Scroll through the table to see each lease period, start and end dates, the depreciation charge and the outstanding amount.
5. Use **Export CSV** or **Export Excel** to download the schedule rows exactly as shown in the table for the current contract.
6. Choose another contract from the dashboard dropdown to compare schedules; the summary and table update automatically.

## Notes
- The table follows lease periods (not repayment periods) and is sorted in the same order as the depreciation run.
- The "Initial amount" for each row after the first now matches the previous period's outstanding balance so the net book value chain is visible without recalculating.
- If the schedule fails to load, an alert appears so you can retry after verifying the contract selection.
- If the schedule loads without amounts, refresh the page; the client now converts snake-case API responses so the figures populate after reload.
