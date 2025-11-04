# Lease Liability Schedule Dashboard – User Guide

The dashboard provides a consolidated view of the amortisation activity for a single IFRS16 lease contract.

## Accessing the dashboard
1. Ensure your account has the **Lease Manager** role.
2. Navigate to the leases workspace and open `#/lease-liability-schedule-view/{contractId}` (replace `{contractId}` with the numeric identifier of the contract).
3. The screen loads with the latest repayment period pre-selected.

## Understanding the layout
- **Header context** – shows the lease title and the liability reference so you can confirm you are reviewing the correct contract.
- **Period selector** – a dropdown listing all repayment periods for the contract. The most recent period is selected automatically.
- **Headline cards** – display the initial liability, schedule start date, reporting close date, and the totals for cash payments, principal, interest, outstanding balance, and interest payable for the active period. All amounts are formatted with the shared numeric pipes.
- **Monthly schedule table** – lists every amortisation row for the selected period, including payment days since the previous row and the opening/cash/principal/interest/outstanding/interest-payable values.

## Working with reporting periods
1. Use the dropdown to switch to a different period. The totals and table update instantly without reloading the page.
2. If a period has no schedule rows, the table shows an informative empty-state message; the headline cards continue to show the last computed totals.
3. Switch back to the latest period at any time to return to the default view.

## Troubleshooting
- If data fails to load, an alert appears at the top of the page. Refresh the browser once the underlying services are available.
- When the selector is disabled, the contract does not yet have repayment periods—capture them first before revisiting the dashboard.

## Exporting to Excel
- Click **Export Excel** to download a workbook that mirrors the on-screen layout.
- The top of the sheet is divided into three panels:
  - **Lease** – title, contract ID, and liability reference so the recipient can identify the contract immediately.
  - **Stats** – the initial liability and the cash, principal, and interest totals for the selected reporting window.
  - **Reporting** – the reporting period label, start and close dates, and the closing outstanding and interest-payable balances.
- The monthly schedule table appears beneath the panels using the same column order as the dashboard for easy reconciliation.
