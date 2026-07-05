import * as dayjs from 'dayjs';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';

export interface ILeaseRepaymentPeriod {
  id?: number;
  sequenceNumber?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  periodCode?: string;
  fiscalMonth?: IFiscalMonth;
}

export class LeaseRepaymentPeriod implements ILeaseRepaymentPeriod {
  constructor(
    public id?: number,
    public sequenceNumber?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public periodCode?: string,
    public fiscalMonth?: IFiscalMonth
  ) {}
}

export function getLeaseRepaymentPeriodIdentifier(leaseRepaymentPeriod: ILeaseRepaymentPeriod): number | undefined {
  return leaseRepaymentPeriod.id;
}
