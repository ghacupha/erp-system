import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';

export interface IProcessStatus {
  id?: number;
  statusCode?: string;
  description?: string;
  placeholders?: IPlaceholder[] | null;
  parameters?: IUniversallyUniqueMapping[] | null;
}

export class ProcessStatus implements IProcessStatus {
  constructor(
    public id?: number,
    public statusCode?: string,
    public description?: string,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getProcessStatusIdentifier(processStatus: IProcessStatus): number | undefined {
  return processStatus.id;
}
