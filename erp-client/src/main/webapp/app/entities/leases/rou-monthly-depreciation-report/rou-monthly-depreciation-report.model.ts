import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';

export interface IRouMonthlyDepreciationReport {
  id?: number;
  requestId?: string;
  timeOfRequest?: dayjs.Dayjs;
  reportIsCompiled?: boolean | null;
  fileChecksum?: string | null;
  tampered?: boolean | null;
  filename?: string | null;
  reportParameters?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  requestedBy?: IApplicationUser | null;
  reportingYear?: IFiscalYear;
}

export class RouMonthlyDepreciationReport implements IRouMonthlyDepreciationReport {
  constructor(
    public id?: number,
    public requestId?: string,
    public timeOfRequest?: dayjs.Dayjs,
    public reportIsCompiled?: boolean | null,
    public fileChecksum?: string | null,
    public tampered?: boolean | null,
    public filename?: string | null,
    public reportParameters?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public requestedBy?: IApplicationUser | null,
    public reportingYear?: IFiscalYear
  ) {
    this.reportIsCompiled = this.reportIsCompiled ?? false;
    this.tampered = this.tampered ?? false;
  }
}

export function getRouMonthlyDepreciationReportIdentifier(rouMonthlyDepreciationReport: IRouMonthlyDepreciationReport): number | undefined {
  return rouMonthlyDepreciationReport.id;
}
