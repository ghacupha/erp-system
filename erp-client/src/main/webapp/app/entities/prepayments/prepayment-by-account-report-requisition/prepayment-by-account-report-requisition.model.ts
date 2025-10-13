///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IPrepaymentByAccountReportRequisition {
  id?: number;
  requestId?: string | null;
  timeOfRequisition?: dayjs.Dayjs;
  fileChecksum?: string | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  reportDate?: dayjs.Dayjs;
  tampered?: boolean | null;
  requestedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
}

export class PrepaymentByAccountReportRequisition implements IPrepaymentByAccountReportRequisition {
  constructor(
    public id?: number,
    public requestId?: string | null,
    public timeOfRequisition?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public reportDate?: dayjs.Dayjs,
    public tampered?: boolean | null,
    public requestedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getPrepaymentByAccountReportRequisitionIdentifier(
  prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition
): number | undefined {
  return prepaymentByAccountReportRequisition.id;
}
