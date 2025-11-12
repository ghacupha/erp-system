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
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IReportStatus } from 'app/entities/reports/report-status/report-status.model';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { ISystemModule } from 'app/entities/system/system-module/system-module.model';
import { IReportDesign } from 'app/entities/reports/report-design/report-design.model';
import { IAlgorithm } from 'app/entities/system/algorithm/algorithm.model';

export interface IExcelReportExport {
  id?: number;
  reportName?: string;
  reportPassword?: string;
  reportNotesContentType?: string | null;
  reportNotes?: string | null;
  fileCheckSum?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  reportTimeStamp?: dayjs.Dayjs;
  reportId?: string;
  placeholders?: IPlaceholder[] | null;
  parameters?: IUniversallyUniqueMapping[] | null;
  reportStatus?: IReportStatus | null;
  securityClearance?: ISecurityClearance;
  reportCreator?: IApplicationUser;
  organization?: IDealer;
  department?: IDealer;
  systemModule?: ISystemModule;
  reportDesign?: IReportDesign;
  fileCheckSumAlgorithm?: IAlgorithm;
}

export class ExcelReportExport implements IExcelReportExport {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportPassword?: string,
    public reportNotesContentType?: string | null,
    public reportNotes?: string | null,
    public fileCheckSum?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public reportTimeStamp?: dayjs.Dayjs,
    public reportId?: string,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IUniversallyUniqueMapping[] | null,
    public reportStatus?: IReportStatus | null,
    public securityClearance?: ISecurityClearance,
    public reportCreator?: IApplicationUser,
    public organization?: IDealer,
    public department?: IDealer,
    public systemModule?: ISystemModule,
    public reportDesign?: IReportDesign,
    public fileCheckSumAlgorithm?: IAlgorithm
  ) {}
}

export function getExcelReportExportIdentifier(excelReportExport: IExcelReportExport): number | undefined {
  return excelReportExport.id;
}
