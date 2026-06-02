import * as dayjs from 'dayjs';
import { ILeasePeriod } from 'app/entities/leases/lease-period/lease-period.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IRouAccountBalanceReport {
  id?: number;
  requestId?: string;
  timeOfRequest?: dayjs.Dayjs | null;
  reportIsCompiled?: boolean | null;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  leasePeriod?: ILeasePeriod;
  requestedBy?: IApplicationUser | null;
}

export class RouAccountBalanceReport implements IRouAccountBalanceReport {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequest?: dayjs.Dayjs | null,
    public reportIsCompiled?: boolean | null,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public leasePeriod?: ILeasePeriod,
    public requestedBy?: IApplicationUser | null
  ) {
    this.reportIsCompiled = this.reportIsCompiled ?? false;
    this.tampered = this.tampered ?? false;
  }
}

export function getRouAccountBalanceReportIdentifier(rouAccountBalanceReport: IRouAccountBalanceReport): number | undefined {
  return rouAccountBalanceReport.id;
}
