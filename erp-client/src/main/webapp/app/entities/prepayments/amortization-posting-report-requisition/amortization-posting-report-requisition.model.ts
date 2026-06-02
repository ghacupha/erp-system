import * as dayjs from 'dayjs';
import { IAmortizationPeriod } from 'app/entities/prepayments/amortization-period/amortization-period.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface IAmortizationPostingReportRequisition {
  id?: number;
  requestId?: string;
  timeOfRequisition?: dayjs.Dayjs;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  amortizationPeriod?: IAmortizationPeriod;
  requestedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
}

export class AmortizationPostingReportRequisition implements IAmortizationPostingReportRequisition {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequisition?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public amortizationPeriod?: IAmortizationPeriod,
    public requestedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getAmortizationPostingReportRequisitionIdentifier(
  amortizationPostingReportRequisition: IAmortizationPostingReportRequisition
): number | undefined {
  return amortizationPostingReportRequisition.id;
}
