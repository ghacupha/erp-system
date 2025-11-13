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

import { IFiscalYear } from '../../erp-pages/fiscal-year/fiscal-year.model';
import * as dayjs from 'dayjs';

export interface IMonthlyPrepaymentReportRequisition {
  id?: number;
  requestId?: string | null;
  timeOfRequisition?: dayjs.Dayjs;
  fileChecksum?: string | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  tampered?: boolean | null;
  fiscalYear?: IFiscalYear;
}

export class MonthlyPrepaymentReportRequisition implements IMonthlyPrepaymentReportRequisition {
  constructor(
    public id?: number,
    public requestId?: string | null,
    public timeOfRequisition?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public tampered?: boolean | null,
    public fiscalYear?: IFiscalYear) {}
}

export function getMonthlyPrepaymentReportRequisitionIdentifier(
  monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition
): number | undefined {
  return monthlyPrepaymentReportRequisition.id;
}
