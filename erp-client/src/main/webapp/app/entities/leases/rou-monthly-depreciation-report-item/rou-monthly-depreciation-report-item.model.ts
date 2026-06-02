import * as dayjs from 'dayjs';

export interface IRouMonthlyDepreciationReportItem {
  id?: number;
  fiscalMonthStartDate?: dayjs.Dayjs | null;
  fiscalMonthEndDate?: dayjs.Dayjs | null;
  totalDepreciationAmount?: number | null;
}

export class RouMonthlyDepreciationReportItem implements IRouMonthlyDepreciationReportItem {
  constructor(
    public id?: number,
    public fiscalMonthStartDate?: dayjs.Dayjs | null,
    public fiscalMonthEndDate?: dayjs.Dayjs | null,
    public totalDepreciationAmount?: number | null
  ) {}
}

export function getRouMonthlyDepreciationReportItemIdentifier(
  rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem
): number | undefined {
  return rouMonthlyDepreciationReportItem.id;
}
