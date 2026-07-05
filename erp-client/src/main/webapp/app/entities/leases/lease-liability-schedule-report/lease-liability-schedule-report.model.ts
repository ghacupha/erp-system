import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ILeaseAmortizationSchedule } from 'app/entities/leases/lease-amortization-schedule/lease-amortization-schedule.model';

export interface ILeaseLiabilityScheduleReport {
  id?: number;
  requestId?: string;
  timeOfRequest?: dayjs.Dayjs;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
  leaseAmortizationSchedule?: ILeaseAmortizationSchedule;
}

export class LeaseLiabilityScheduleReport implements ILeaseLiabilityScheduleReport {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequest?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null,
    public leaseAmortizationSchedule?: ILeaseAmortizationSchedule
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getLeaseLiabilityScheduleReportIdentifier(leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport): number | undefined {
  return leaseLiabilityScheduleReport.id;
}
