import * as dayjs from 'dayjs';

export interface IRouAccountBalanceReportItem {
  id?: number;
  assetAccountName?: string | null;
  assetAccountNumber?: string | null;
  depreciationAccountNumber?: string | null;
  totalLeaseAmount?: number | null;
  accruedDepreciationAmount?: number | null;
  currentPeriodDepreciationAmount?: number | null;
  netBookValue?: number | null;
  fiscalPeriodEndDate?: dayjs.Dayjs | null;
}

export class RouAccountBalanceReportItem implements IRouAccountBalanceReportItem {
  constructor(
    public id?: number,
    public assetAccountName?: string | null,
    public assetAccountNumber?: string | null,
    public depreciationAccountNumber?: string | null,
    public totalLeaseAmount?: number | null,
    public accruedDepreciationAmount?: number | null,
    public currentPeriodDepreciationAmount?: number | null,
    public netBookValue?: number | null,
    public fiscalPeriodEndDate?: dayjs.Dayjs | null
  ) {}
}

export function getRouAccountBalanceReportItemIdentifier(rouAccountBalanceReportItem: IRouAccountBalanceReportItem): number | undefined {
  return rouAccountBalanceReportItem.id;
}
