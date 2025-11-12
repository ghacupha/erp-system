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
