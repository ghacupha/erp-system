import * as dayjs from 'dayjs';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';

export interface IAmortizationPeriod {
  id?: number;
  sequenceNumber?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  periodCode?: string;
  fiscalMonth?: IFiscalMonth;
  amortizationPeriod?: IAmortizationPeriod;
}

export class AmortizationPeriod implements IAmortizationPeriod {
  constructor(
    public id?: number,
    public sequenceNumber?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public periodCode?: string,
    public fiscalMonth?: IFiscalMonth,
    public amortizationPeriod?: IAmortizationPeriod
  ) {}
}

export function getAmortizationPeriodIdentifier(amortizationPeriod: IAmortizationPeriod): number | undefined {
  return amortizationPeriod.id;
}
