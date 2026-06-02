import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { depreciationProcessStatusTypes } from 'app/entities/enumerations/depreciation-process-status-types.model';

export interface IRouDepreciationRequest {
  id?: number;
  requisitionId?: string;
  timeOfRequest?: dayjs.Dayjs | null;
  depreciationProcessStatus?: depreciationProcessStatusTypes | null;
  numberOfEnumeratedItems?: number | null;
  invalidated?: boolean | null;
  initiatedBy?: IApplicationUser | null;
}

export class RouDepreciationRequest implements IRouDepreciationRequest {
  constructor(
    public id?: number,
    public requisitionId?: string,
    public timeOfRequest?: dayjs.Dayjs | null,
    public depreciationProcessStatus?: depreciationProcessStatusTypes | null,
    public numberOfEnumeratedItems?: number | null,
    public invalidated?: boolean | null,
    public initiatedBy?: IApplicationUser | null
  ) {
    this.invalidated = this.invalidated ?? false;
  }
}

export function getRouDepreciationRequestIdentifier(rouDepreciationRequest: IRouDepreciationRequest): number | undefined {
  return rouDepreciationRequest.id;
}
