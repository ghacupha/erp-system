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
import { DepreciationJobStatusType } from '../../erp-common/enumerations/depreciation-job-status-type.model';
import { IDepreciationPeriod } from '../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';

export interface IDepreciationJob {
  id?: number;
  timeOfCommencement?: dayjs.Dayjs | null;
  depreciationJobStatus?: DepreciationJobStatusType | null;
  description?: string | null;
  numberOfBatches?: number | null;
  processedBatches?: number | null;
  lastBatchSize?: number | null;
  processedItems?: number | null;
  processingTime?: string | null;
  totalItems?: number | null;
  createdBy?: IApplicationUser | null;
  depreciationPeriod?: IDepreciationPeriod | null;
}

export class DepreciationJob implements IDepreciationJob {
  constructor(
    public id?: number,
    public timeOfCommencement?: dayjs.Dayjs | null,
    public depreciationJobStatus?: DepreciationJobStatusType | null,
    public description?: string | null,
    public numberOfBatches?: number | null,
    public processedBatches?: number | null,
    public lastBatchSize?: number | null,
    public processedItems?: number | null,
    public processingTime?: string | null,
    public totalItems?: number | null,
    public createdBy?: IApplicationUser | null,
    public depreciationPeriod?: IDepreciationPeriod | null
  ) {}
}

export function getDepreciationJobIdentifier(depreciationJob: IDepreciationJob): number | undefined {
  return depreciationJob.id;
}
