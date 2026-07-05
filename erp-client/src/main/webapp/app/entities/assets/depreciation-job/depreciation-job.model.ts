import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { DepreciationJobStatusType } from 'app/entities/enumerations/depreciation-job-status-type.model';

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
