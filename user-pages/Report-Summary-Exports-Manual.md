# User Manual: Exporting Report Summaries

## Purpose
This guide explains how analysts can export any report that uses the report summary view, starting with the **ROU Asset Balance by Account** report. The export actions generate a full data extract from the backing API, bypassing table pagination limits in the UI.

## Prerequisites
- Access to the ERP web client with permission to view the target report.
- A configured report metadata record that exposes a backend API path for the summary view.

## Export Workflow
1. Navigate to **ROU Reports ▸ ROU Asset Balance Report** (or any report that uses the summary view component).
2. Apply filters such as lease period or lease liability. The component re-queries the backend using those parameters.
3. Review the table results. Once the data is ready, choose one of the export buttons in the top-right corner of the summary header:
   - **Export CSV** – Creates a comma-separated text file that preserves the visible column order.
   - **Export Excel** – Generates an `.xlsx` workbook with the same columns and formatted values.
4. Wait for the spinner beside the button to finish. The browser will download the file automatically using a timestamped filename (e.g., `rou-asset-balance-report-20251024-161500.csv`).

## Behaviour Details
- Both export options call the underlying backend API with pagination paging through all available pages until the full dataset is retrieved.
- The exported columns align with the column order rendered in the UI. Any additional fields returned by subsequent pages are appended to maintain a complete dataset.
- Number and date values use the same formatting as the on-screen table so exported files can be reconciled quickly.

## Troubleshooting
- If the report metadata lacks a backend API path, the export buttons remain disabled and a message appears when you attempt to export.
- When the backend returns no rows for the selected filters, the component displays **"No data is available for export with the current filters"** instead of downloading an empty file.
- API errors (network outages, timeouts, or server failures) surface as **"Unable to export report data. Please try again later."** and the buttons re-enable once the request completes.

## Related Components
- `src/main/webapp/app/erp/erp-reports/report-summary-view/report-summary-view.component.ts`
- `src/main/webapp/app/erp/erp-reports/report-summary-view/report-summary-data.service.ts`
- `src/main/webapp/app/erp/erp-reports/report-summary-view/report-summary-export.util.ts`
