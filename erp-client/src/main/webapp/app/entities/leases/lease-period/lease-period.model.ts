import * as dayjs from 'dayjs';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';

export interface ILeasePeriod {
  id?: number;
  sequenceNumber?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  periodCode?: string;
  fiscalMonth?: IFiscalMonth;
}

export class LeasePeriod implements ILeasePeriod {
  constructor(
    public id?: number,
    public sequenceNumber?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public periodCode?: string,
    public fiscalMonth?: IFiscalMonth
  ) {}
}

export function getLeasePeriodIdentifier(leasePeriod: ILeasePeriod): number | undefined {
  return leasePeriod.id;
}
