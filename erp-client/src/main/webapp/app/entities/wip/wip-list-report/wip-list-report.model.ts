import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IWIPListReport {
  id?: number;
  timeOfRequest?: dayjs.Dayjs;
  requestId?: string;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
}

export class WIPListReport implements IWIPListReport {
  constructor(
    public id?: number,
    public timeOfRequest?: dayjs.Dayjs,
    public requestId?: string,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getWIPListReportIdentifier(wIPListReport: IWIPListReport): number | undefined {
  return wIPListReport.id;
}
