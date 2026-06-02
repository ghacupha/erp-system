import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IWIPTransferListReport {
  id?: number;
  timeOfRequest?: dayjs.Dayjs;
  requestId?: string;
  fileChecksum?: string | null;
  tempered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
}

export class WIPTransferListReport implements IWIPTransferListReport {
  constructor(
    public id?: number,
    public timeOfRequest?: dayjs.Dayjs,
    public requestId?: string,
    public fileChecksum?: string | null,
    public tempered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null
  ) {
    this.tempered = this.tempered ?? false;
  }
}

export function getWIPTransferListReportIdentifier(wIPTransferListReport: IWIPTransferListReport): number | undefined {
  return wIPTransferListReport.id;
}
