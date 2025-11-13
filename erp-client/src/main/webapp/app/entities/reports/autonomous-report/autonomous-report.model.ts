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
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IAutonomousReport {
  id?: number;
  reportName?: string;
  reportParameters?: string | null;
  createdAt?: dayjs.Dayjs;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  reportTampered?: boolean | null;
  reportMappings?: IUniversallyUniqueMapping[] | null;
  placeholders?: IPlaceholder[] | null;
  createdBy?: IApplicationUser | null;
}

export class AutonomousReport implements IAutonomousReport {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportParameters?: string | null,
    public createdAt?: dayjs.Dayjs,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public reportTampered?: boolean | null,
    public reportMappings?: IUniversallyUniqueMapping[] | null,
    public placeholders?: IPlaceholder[] | null,
    public createdBy?: IApplicationUser | null
  ) {
    this.reportTampered = this.reportTampered ?? false;
  }
}

export function getAutonomousReportIdentifier(autonomousReport: IAutonomousReport): number | undefined {
  return autonomousReport.id;
}
