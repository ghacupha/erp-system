import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IWorkInProgressOutstandingReportRequisition {
  id?: number;
  requestId?: string;
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

export class WorkInProgressOutstandingReportRequisition implements IWorkInProgressOutstandingReportRequisition {
  constructor(
    public id?: number,
    public requestId?: string,
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

export function getWorkInProgressOutstandingReportRequisitionIdentifier(
  workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition
): number | undefined {
  return workInProgressOutstandingReportRequisition.id;
}
