import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IRouDepreciationEntryReport {
  id?: number;
  requestId?: string;
  timeOfRequest?: dayjs.Dayjs | null;
  reportIsCompiled?: boolean | null;
  periodStartDate?: dayjs.Dayjs | null;
  periodEndDate?: dayjs.Dayjs | null;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
}

export class RouDepreciationEntryReport implements IRouDepreciationEntryReport {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequest?: dayjs.Dayjs | null,
    public reportIsCompiled?: boolean | null,
    public periodStartDate?: dayjs.Dayjs | null,
    public periodEndDate?: dayjs.Dayjs | null,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null
  ) {
    this.reportIsCompiled = this.reportIsCompiled ?? false;
    this.tampered = this.tampered ?? false;
  }
}

export function getRouDepreciationEntryReportIdentifier(rouDepreciationEntryReport: IRouDepreciationEntryReport): number | undefined {
  return rouDepreciationEntryReport.id;
}
