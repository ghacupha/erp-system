import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IPrepaymentReportRequisition {
  id?: number;
  reportName?: string;
  reportDate?: dayjs.Dayjs;
  timeOfRequisition?: dayjs.Dayjs;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
}

export class PrepaymentReportRequisition implements IPrepaymentReportRequisition {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportDate?: dayjs.Dayjs,
    public timeOfRequisition?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getPrepaymentReportRequisitionIdentifier(prepaymentReportRequisition: IPrepaymentReportRequisition): number | undefined {
  return prepaymentReportRequisition.id;
}
