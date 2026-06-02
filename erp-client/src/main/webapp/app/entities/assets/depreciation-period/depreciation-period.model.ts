import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';
import { DepreciationPeriodStatusTypes } from 'app/entities/enumerations/depreciation-period-status-types.model';

export interface IDepreciationPeriod {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  depreciationPeriodStatus?: DepreciationPeriodStatusTypes | null;
  periodCode?: string;
  processLocked?: boolean | null;
  previousPeriod?: IDepreciationPeriod | null;
  createdBy?: IApplicationUser | null;
  fiscalYear?: IFiscalYear;
  fiscalMonth?: IFiscalMonth;
  fiscalQuarter?: IFiscalQuarter;
}

export class DepreciationPeriod implements IDepreciationPeriod {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public depreciationPeriodStatus?: DepreciationPeriodStatusTypes | null,
    public periodCode?: string,
    public processLocked?: boolean | null,
    public previousPeriod?: IDepreciationPeriod | null,
    public createdBy?: IApplicationUser | null,
    public fiscalYear?: IFiscalYear,
    public fiscalMonth?: IFiscalMonth,
    public fiscalQuarter?: IFiscalQuarter
  ) {
    this.processLocked = this.processLocked ?? false;
  }
}

export function getDepreciationPeriodIdentifier(depreciationPeriod: IDepreciationPeriod): number | undefined {
  return depreciationPeriod.id;
}
