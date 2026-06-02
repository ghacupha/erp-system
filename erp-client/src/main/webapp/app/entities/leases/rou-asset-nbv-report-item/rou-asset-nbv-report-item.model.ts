import * as dayjs from 'dayjs';

export interface IRouAssetNBVReportItem {
  id?: number;
  modelTitle?: string | null;
  modelVersion?: number | null;
  description?: string | null;
  rouModelReference?: string | null;
  commencementDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  assetCategoryName?: string | null;
  assetAccountNumber?: string | null;
  depreciationAccountNumber?: string | null;
  fiscalPeriodEndDate?: dayjs.Dayjs | null;
  leaseAmount?: number | null;
  netBookValue?: number | null;
}

export class RouAssetNBVReportItem implements IRouAssetNBVReportItem {
  constructor(
    public id?: number,
    public modelTitle?: string | null,
    public modelVersion?: number | null,
    public description?: string | null,
    public rouModelReference?: string | null,
    public commencementDate?: dayjs.Dayjs | null,
    public expirationDate?: dayjs.Dayjs | null,
    public assetCategoryName?: string | null,
    public assetAccountNumber?: string | null,
    public depreciationAccountNumber?: string | null,
    public fiscalPeriodEndDate?: dayjs.Dayjs | null,
    public leaseAmount?: number | null,
    public netBookValue?: number | null
  ) {}
}

export function getRouAssetNBVReportItemIdentifier(rouAssetNBVReportItem: IRouAssetNBVReportItem): number | undefined {
  return rouAssetNBVReportItem.id;
}
