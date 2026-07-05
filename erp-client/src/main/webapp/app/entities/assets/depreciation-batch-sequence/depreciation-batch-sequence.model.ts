import * as dayjs from 'dayjs';
import { IDepreciationJob } from 'app/entities/assets/depreciation-job/depreciation-job.model';
import { DepreciationBatchStatusType } from 'app/entities/enumerations/depreciation-batch-status-type.model';

export interface IDepreciationBatchSequence {
  id?: number;
  startIndex?: number | null;
  endIndex?: number | null;
  createdAt?: dayjs.Dayjs | null;
  depreciationBatchStatus?: DepreciationBatchStatusType | null;
  batchSize?: number | null;
  processedItems?: number | null;
  sequenceNumber?: number | null;
  isLastBatch?: boolean | null;
  processingTime?: string | null;
  totalItems?: number | null;
  depreciationJob?: IDepreciationJob | null;
}

export class DepreciationBatchSequence implements IDepreciationBatchSequence {
  constructor(
    public id?: number,
    public startIndex?: number | null,
    public endIndex?: number | null,
    public createdAt?: dayjs.Dayjs | null,
    public depreciationBatchStatus?: DepreciationBatchStatusType | null,
    public batchSize?: number | null,
    public processedItems?: number | null,
    public sequenceNumber?: number | null,
    public isLastBatch?: boolean | null,
    public processingTime?: string | null,
    public totalItems?: number | null,
    public depreciationJob?: IDepreciationJob | null
  ) {
    this.isLastBatch = this.isLastBatch ?? false;
  }
}

export function getDepreciationBatchSequenceIdentifier(depreciationBatchSequence: IDepreciationBatchSequence): number | undefined {
  return depreciationBatchSequence.id;
}
