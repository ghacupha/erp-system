import * as dayjs from 'dayjs';

export interface IMonthlyPrepaymentOutstandingReportItem {
  id?: number;
  fiscalMonthEndDate?: dayjs.Dayjs | null;
  totalPrepaymentAmount?: number | null;
  totalAmortisedAmount?: number | null;
  totalOutstandingAmount?: number | null;
  numberOfPrepaymentAccounts?: number | null;
}

export class MonthlyPrepaymentOutstandingReportItem implements IMonthlyPrepaymentOutstandingReportItem {
  constructor(
    public id?: number,
    public fiscalMonthEndDate?: dayjs.Dayjs | null,
    public totalPrepaymentAmount?: number | null,
    public totalAmortisedAmount?: number | null,
    public totalOutstandingAmount?: number | null,
    public numberOfPrepaymentAccounts?: number | null
  ) {}
}

export function getMonthlyPrepaymentOutstandingReportItemIdentifier(
  monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem
): number | undefined {
  return monthlyPrepaymentOutstandingReportItem.id;
}
