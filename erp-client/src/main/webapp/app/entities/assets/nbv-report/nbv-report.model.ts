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

import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';

export interface INbvReport {
  id?: number;
  reportName?: string;
  timeOfReportRequest?: dayjs.Dayjs;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
  depreciationPeriod?: IDepreciationPeriod;
  serviceOutlet?: IServiceOutlet | null;
  assetCategory?: IAssetCategory | null;
}

export class NbvReport implements INbvReport {
  constructor(
    public id?: number,
    public reportName?: string,
    public timeOfReportRequest?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null,
    public depreciationPeriod?: IDepreciationPeriod,
    public serviceOutlet?: IServiceOutlet | null,
    public assetCategory?: IAssetCategory | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getNbvReportIdentifier(nbvReport: INbvReport): number | undefined {
  return nbvReport.id;
}
