///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import * as dayjs from 'dayjs';

export interface IWorkInProgressOutstandingReport {
  id?: number;
  sequenceNumber?: string | null;
  particulars?: string | null;
  dealerName?: string | null;
  instalmentTransactionNumber?: string | null;
  instalmentTransactionDate?: dayjs.Dayjs | null;
  iso4217Code?: string | null;
  instalmentAmount?: number | null;
  totalTransferAmount?: number | null;
  outstandingAmount?: number | null;
}

export class WorkInProgressOutstandingReport implements IWorkInProgressOutstandingReport {
  constructor(
    public id?: number,
    public sequenceNumber?: string | null,
    public particulars?: string | null,
    public dealerName?: string | null,
    public instalmentTransactionNumber?: string | null,
    public instalmentTransactionDate?: dayjs.Dayjs | null,
    public iso4217Code?: string | null,
    public instalmentAmount?: number | null,
    public totalTransferAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getWorkInProgressOutstandingReportIdentifier(
  workInProgressOutstandingReport: IWorkInProgressOutstandingReport
): number | undefined {
  return workInProgressOutstandingReport.id;
}
