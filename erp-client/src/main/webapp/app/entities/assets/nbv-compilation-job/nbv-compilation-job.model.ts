import * as dayjs from 'dayjs';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { NVBCompilationStatus } from 'app/entities/enumerations/nvb-compilation-status.model';

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
