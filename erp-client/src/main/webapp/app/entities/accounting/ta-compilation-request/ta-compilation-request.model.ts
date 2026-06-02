import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { compilationProcessStatusTypes } from 'app/entities/enumerations/compilation-process-status-types.model';

export interface ITACompilationRequest {
  id?: number;
  requisitionId?: string;
  timeOfRequest?: dayjs.Dayjs | null;
  compilationProcessStatus?: compilationProcessStatusTypes | null;
  numberOfEnumeratedItems?: number | null;
  batchJobIdentifier?: string;
  compilationTime?: dayjs.Dayjs | null;
  invalidated?: boolean | null;
  initiatedBy?: IApplicationUser | null;
}

export class TACompilationRequest implements ITACompilationRequest {
  constructor(
    public id?: number,
    public requisitionId?: string,
    public timeOfRequest?: dayjs.Dayjs | null,
    public compilationProcessStatus?: compilationProcessStatusTypes | null,
    public numberOfEnumeratedItems?: number | null,
    public batchJobIdentifier?: string,
    public compilationTime?: dayjs.Dayjs | null,
    public invalidated?: boolean | null,
    public initiatedBy?: IApplicationUser | null
  ) {
    this.invalidated = this.invalidated ?? false;
  }
}

export function getTACompilationRequestIdentifier(tACompilationRequest: ITACompilationRequest): number | undefined {
  return tACompilationRequest.id;
}
