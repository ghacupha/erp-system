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
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';

export interface IAssetAdditionsReport {
  id?: number;
  timeOfRequest?: dayjs.Dayjs | null;
  reportStartDate?: dayjs.Dayjs | null;
  reportEndDate?: dayjs.Dayjs | null;
  requestId?: string | null;
  tampered?: boolean | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
}

export class AssetAdditionsReport implements IAssetAdditionsReport {
  constructor(
    public id?: number,
    public timeOfRequest?: dayjs.Dayjs | null,
    public reportStartDate?: dayjs.Dayjs | null,
    public reportEndDate?: dayjs.Dayjs | null,
    public requestId?: string | null,
    public tampered?: boolean | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getAssetAdditionsReportIdentifier(assetAdditionsReport: IAssetAdditionsReport): number | undefined {
  return assetAdditionsReport.id;
}
