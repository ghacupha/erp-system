import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface ILeaseLiabilityCompilation {
  id?: number;
  requestId?: string;
  timeOfRequest?: dayjs.Dayjs;
  requestedBy?: IApplicationUser | null;
}

export class LeaseLiabilityCompilation implements ILeaseLiabilityCompilation {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequest?: dayjs.Dayjs,
    public requestedBy?: IApplicationUser | null
  ) {}
}

export function getLeaseLiabilityCompilationIdentifier(leaseLiabilityCompilation: ILeaseLiabilityCompilation): number | undefined {
  return leaseLiabilityCompilation.id;
}
