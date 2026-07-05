import * as dayjs from 'dayjs';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';

export interface IMonthlyPrepaymentReportRequisition {
  id?: number;
  requestId?: string | null;
  timeOfRequisition?: dayjs.Dayjs;
  fileChecksum?: string | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  tampered?: boolean | null;
  fiscalYear?: IFiscalYear;
}

export class MonthlyPrepaymentReportRequisition implements IMonthlyPrepaymentReportRequisition {
  constructor(
    public id?: number,
    public requestId?: string | null,
    public timeOfRequisition?: dayjs.Dayjs,
    public fileChecksum?: string | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public tampered?: boolean | null,
    public fiscalYear?: IFiscalYear
  ) {
    this.tampered = this.tampered ?? false;
  }
}

export function getMonthlyPrepaymentReportRequisitionIdentifier(
  monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition
): number | undefined {
  return monthlyPrepaymentReportRequisition.id;
}
