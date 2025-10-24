import * as XLSX from 'xlsx';

import { ReportSummaryRecord } from '../report-metadata/report-metadata.model';
import { buildCsvContent, buildExcelArrayBuffer, deriveOrderedColumns } from './report-summary-export.util';

describe('ReportSummaryExportUtil', () => {
  it('should merge existing column order with new record keys', () => {
    const records: ReportSummaryRecord[] = [
      { existing: 'value', additional: 'extra' },
      { another: 'second' },
    ];

    const result = deriveOrderedColumns(records, ['existing']);

    expect(result).toEqual(['existing', 'additional', 'another']);
  });

  it('should build CSV content with escaped values', () => {
    const records: ReportSummaryRecord[] = [{ name: 'Widget, Inc.', amount: 42 }];
    const csv = buildCsvContent(records, ['name', 'amount'], value => String(value ?? ''));

    expect(csv.split('\r\n')).toEqual(['"name","amount"', '"Widget, Inc.","42"']);
  });

  it('should produce an Excel worksheet containing the provided data', () => {
    const records: ReportSummaryRecord[] = [{ name: 'Alpha', amount: 99 }];

    const buffer = buildExcelArrayBuffer(records, ['name', 'amount'], value => String(value ?? ''));
    const workbook = XLSX.read(buffer, { type: 'array' });
    const sheet = workbook.Sheets[workbook.SheetNames[0]];

    expect(sheet['A1'].v).toBe('name');
    expect(sheet['B1'].v).toBe('amount');
    expect(sheet['A2'].v).toBe('Alpha');
    expect(sheet['B2'].v).toBe('99');
  });
});
