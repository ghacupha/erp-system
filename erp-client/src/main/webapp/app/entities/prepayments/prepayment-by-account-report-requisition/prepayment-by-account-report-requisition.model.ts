import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IPrepaymentByAccountReportRequisition {
  id?: number;
  requestId?: string | null;
  timeOfRequisition?: dayjs.Dayjs;
  fileChecksum?: string | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  reportDate?: dayjs.Dayjs;
  tampered?: boolean | null;
  requestedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
}

export class PrepaymentByAccountReportRequisition implements IPrepaymentByAccountReportRequisition {
  constructor(
    public id?: number,
    public requestId?: string | null,
    public timeOfRequisition?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public reportDate?: dayjs.Dayjs,
    public tampered?: boolean | null,
    public requestedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getPrepaymentByAccountReportRequisitionIdentifier(
  prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition
): number | undefined {
  return prepaymentByAccountReportRequisition.id;
}
