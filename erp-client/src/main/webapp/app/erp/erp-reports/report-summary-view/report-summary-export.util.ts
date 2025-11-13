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

type ValueFormatter = (value: unknown) => string;

export function deriveOrderedColumns(records: ReportSummaryRecord[], existingOrder: string[] = []): string[] {
  const ordered = [...existingOrder];
  const seen = new Set(ordered);

  records.forEach(record => {
    Object.keys(record).forEach(key => {
      if (!seen.has(key)) {
        seen.add(key);
        ordered.push(key);
      }
    });
  });

  return ordered;
}

export function buildCsvContent(
  records: ReportSummaryRecord[],
  columns: string[],
  formatValue: ValueFormatter
): string {
  const escape = (value: string): string => {
    const needsEscaping = /[",\n\r]/.test(value);
    const escapedValue = value.replace(/"/g, '""');
    return needsEscaping ? `"${escapedValue}"` : escapedValue;
  };

  const header = columns.map(column => escape(column));
  const rows = records.map(record => columns.map(column => escape(formatValue(record[column]))));

  return [header, ...rows].map(row => row.join(',')).join('\r\n');
}

export function buildExcelArrayBuffer(
  records: ReportSummaryRecord[],
  columns: string[],
  formatValue: ValueFormatter
): ArrayBuffer {
  const worksheetData = [columns, ...records.map(record => columns.map(column => formatValue(record[column])))] as string[][];
  const worksheet = XLSX.utils.aoa_to_sheet(worksheetData);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Report Summary');
  return XLSX.write(workbook, { type: 'array', bookType: 'xlsx' });
}
