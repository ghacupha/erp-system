import * as dayjs from 'dayjs';

export interface IRouDepreciationEntryReportItem {
  id?: number;
  leaseContractNumber?: string | null;
  fiscalPeriodCode?: string | null;
  fiscalPeriodEndDate?: dayjs.Dayjs | null;
  assetCategoryName?: string | null;
  debitAccountNumber?: string | null;
  creditAccountNumber?: string | null;
  description?: string | null;
  shortTitle?: string | null;
  rouAssetIdentifier?: string | null;
  sequenceNumber?: number | null;
  depreciationAmount?: number | null;
  outstandingAmount?: number | null;
}

export class RouDepreciationEntryReportItem implements IRouDepreciationEntryReportItem {
  constructor(
    public id?: number,
    public leaseContractNumber?: string | null,
    public fiscalPeriodCode?: string | null,
    public fiscalPeriodEndDate?: dayjs.Dayjs | null,
    public assetCategoryName?: string | null,
    public debitAccountNumber?: string | null,
    public creditAccountNumber?: string | null,
    public description?: string | null,
    public shortTitle?: string | null,
    public rouAssetIdentifier?: string | null,
    public sequenceNumber?: number | null,
    public depreciationAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getRouDepreciationEntryReportItemIdentifier(
  rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem
): number | undefined {
  return rouDepreciationEntryReportItem.id;
}
