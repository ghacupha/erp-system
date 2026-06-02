import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { CompilationStatusTypes } from 'app/entities/enumerations/compilation-status-types.model';

export interface IPrepaymentCompilationRequest {
  id?: number;
  timeOfRequest?: dayjs.Dayjs | null;
  compilationStatus?: CompilationStatusTypes | null;
  itemsProcessed?: number | null;
  compilationToken?: string;
  narration?: string | null;
  postedBy?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class PrepaymentCompilationRequest implements IPrepaymentCompilationRequest {
  constructor(
    public id?: number,
    public timeOfRequest?: dayjs.Dayjs | null,
    public compilationStatus?: CompilationStatusTypes | null,
    public itemsProcessed?: number | null,
    public compilationToken?: string,
    public narration?: string | null,
    public postedBy?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPrepaymentCompilationRequestIdentifier(prepaymentCompilationRequest: IPrepaymentCompilationRequest): number | undefined {
  return prepaymentCompilationRequest.id;
}
