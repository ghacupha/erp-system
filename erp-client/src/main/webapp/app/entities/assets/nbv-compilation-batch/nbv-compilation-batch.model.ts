import { INbvCompilationJob } from 'app/entities/assets/nbv-compilation-job/nbv-compilation-job.model';
import { CompilationBatchStatusTypes } from 'app/entities/enumerations/compilation-batch-status-types.model';

export interface INbvCompilationBatch {
  id?: number;
  startIndex?: number | null;
  endIndex?: number | null;
  compilationBatchStatus?: CompilationBatchStatusTypes | null;
  compilationBatchIdentifier?: string | null;
  compilationJobidentifier?: string | null;
  depreciationPeriodIdentifier?: string | null;
  fiscalMonthIdentifier?: string | null;
  batchSize?: number | null;
  processedItems?: number | null;
  sequenceNumber?: number | null;
  isLastBatch?: boolean | null;
  processingTime?: string | null;
  totalItems?: number | null;
  nbvCompilationJob?: INbvCompilationJob | null;
}

export class NbvCompilationBatch implements INbvCompilationBatch {
  constructor(
    public id?: number,
    public startIndex?: number | null,
    public endIndex?: number | null,
    public compilationBatchStatus?: CompilationBatchStatusTypes | null,
    public compilationBatchIdentifier?: string | null,
    public compilationJobidentifier?: string | null,
    public depreciationPeriodIdentifier?: string | null,
    public fiscalMonthIdentifier?: string | null,
    public batchSize?: number | null,
    public processedItems?: number | null,
    public sequenceNumber?: number | null,
    public isLastBatch?: boolean | null,
    public processingTime?: string | null,
    public totalItems?: number | null,
    public nbvCompilationJob?: INbvCompilationJob | null
  ) {
    this.isLastBatch = this.isLastBatch ?? false;
  }
}

export function getNbvCompilationBatchIdentifier(nbvCompilationBatch: INbvCompilationBatch): number | undefined {
  return nbvCompilationBatch.id;
}
