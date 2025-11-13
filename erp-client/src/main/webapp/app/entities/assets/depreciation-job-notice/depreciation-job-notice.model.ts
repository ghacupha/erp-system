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
import { IDepreciationJob } from 'app/entities/assets/depreciation-job/depreciation-job.model';
import { IDepreciationBatchSequence } from 'app/entities/assets/depreciation-batch-sequence/depreciation-batch-sequence.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { DepreciationNoticeStatusType } from 'app/entities/enumerations/depreciation-notice-status-type.model';

export interface IDepreciationJobNotice {
  id?: number;
  eventNarrative?: string;
  eventTimeStamp?: dayjs.Dayjs;
  depreciationNoticeStatus?: DepreciationNoticeStatusType;
  sourceModule?: string | null;
  sourceEntity?: string | null;
  errorCode?: string | null;
  errorMessage?: string | null;
  userAction?: string | null;
  technicalDetails?: string | null;
  depreciationJob?: IDepreciationJob | null;
  depreciationBatchSequence?: IDepreciationBatchSequence | null;
  depreciationPeriod?: IDepreciationPeriod | null;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  superintended?: IApplicationUser | null;
}

export class DepreciationJobNotice implements IDepreciationJobNotice {
  constructor(
    public id?: number,
    public eventNarrative?: string,
    public eventTimeStamp?: dayjs.Dayjs,
    public depreciationNoticeStatus?: DepreciationNoticeStatusType,
    public sourceModule?: string | null,
    public sourceEntity?: string | null,
    public errorCode?: string | null,
    public errorMessage?: string | null,
    public userAction?: string | null,
    public technicalDetails?: string | null,
    public depreciationJob?: IDepreciationJob | null,
    public depreciationBatchSequence?: IDepreciationBatchSequence | null,
    public depreciationPeriod?: IDepreciationPeriod | null,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public superintended?: IApplicationUser | null
  ) {}
}

export function getDepreciationJobNoticeIdentifier(depreciationJobNotice: IDepreciationJobNotice): number | undefined {
  return depreciationJobNotice.id;
}
