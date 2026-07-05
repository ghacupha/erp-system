///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
