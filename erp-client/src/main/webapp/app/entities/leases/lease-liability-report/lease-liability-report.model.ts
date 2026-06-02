import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ILeasePeriod } from 'app/entities/leases/lease-period/lease-period.model';

export interface ILeaseLiabilityReport {
  id?: number;
  requestId?: string;
  timeOfRequest?: dayjs.Dayjs | null;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
  leasePeriod?: ILeasePeriod;
}

export class LeaseLiabilityReport implements ILeaseLiabilityReport {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequest?: dayjs.Dayjs | null,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null,
    public leasePeriod?: ILeasePeriod
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getLeaseLiabilityReportIdentifier(leaseLiabilityReport: ILeaseLiabilityReport): number | undefined {
  return leaseLiabilityReport.id;
}
