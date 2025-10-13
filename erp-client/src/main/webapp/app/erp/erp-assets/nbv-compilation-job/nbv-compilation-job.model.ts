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
import { NVBCompilationStatus } from '../../erp-common/enumerations/nvb-compilation-status.model';
import { IDepreciationPeriod } from '../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';

export interface INbvCompilationJob {
  id?: number;
  compilationJobIdentifier?: string;
  compilationJobTimeOfRequest?: dayjs.Dayjs | null;
  entitiesAffected?: number | null;
  compilationStatus?: NVBCompilationStatus | null;
  jobTitle?: string;
  numberOfBatches?: number | null;
  numberOfProcessedBatches?: number | null;
  processingTime?: string | null;
  activePeriod?: IDepreciationPeriod | null;
  initiatedBy?: IApplicationUser | null;
}

export class NbvCompilationJob implements INbvCompilationJob {
  constructor(
    public id?: number,
    public compilationJobIdentifier?: string,
    public compilationJobTimeOfRequest?: dayjs.Dayjs | null,
    public entitiesAffected?: number | null,
    public compilationStatus?: NVBCompilationStatus | null,
    public jobTitle?: string,
    public numberOfBatches?: number | null,
    public numberOfProcessedBatches?: number | null,
    public processingTime?: string | null,
    public activePeriod?: IDepreciationPeriod | null,
    public initiatedBy?: IApplicationUser | null
  ) {}
}

export function getNbvCompilationJobIdentifier(nbvCompilationJob: INbvCompilationJob): number | undefined {
  return nbvCompilationJob.id;
}
