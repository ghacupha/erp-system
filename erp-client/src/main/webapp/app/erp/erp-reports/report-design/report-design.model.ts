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

import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IAlgorithm } from '../../erp-pages/algorithm/algorithm.model';
import { ISecurityClearance } from '../../erp-pages/security-clearance/security-clearance.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';
import { ISystemModule } from '../../erp-pages/system-module/system-module.model';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';

export interface IReportDesign {
  id?: number;
  catalogueNumber?: string;
  designation?: string;
  description?: string | null;
  notesContentType?: string | null;
  notes?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  reportFileChecksum?: string | null;
  parameters?: IUniversallyUniqueMapping[] | null;
  securityClearance?: ISecurityClearance;
  reportDesigner?: IApplicationUser;
  organization?: IDealer;
  department?: IDealer;
  placeholders?: IPlaceholder[] | null;
  systemModule?: ISystemModule;
  fileCheckSumAlgorithm?: IAlgorithm;
}

export class ReportDesign implements IReportDesign {
  constructor(
    public id?: number,
    public catalogueNumber?: string,
    public designation?: string,
    public description?: string | null,
    public notesContentType?: string | null,
    public notes?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public reportFileChecksum?: string | null,
    public parameters?: IUniversallyUniqueMapping[] | null,
    public securityClearance?: ISecurityClearance,
    public reportDesigner?: IApplicationUser,
    public organization?: IDealer,
    public department?: IDealer,
    public placeholders?: IPlaceholder[] | null,
    public systemModule?: ISystemModule,
    public fileCheckSumAlgorithm?: IAlgorithm
  ) {}
}

export function getReportDesignIdentifier(reportDesign: IReportDesign): number | undefined {
  return reportDesign.id;
}
