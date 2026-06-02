import * as dayjs from 'dayjs';

export interface IRouAssetListReportItem {
  id?: number;
  modelTitle?: string | null;
  modelVersion?: number | null;
  description?: string | null;
  leaseTermPeriods?: number | null;
  rouModelReference?: string | null;
  commencementDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  leaseContractTitle?: string | null;
  assetAccountNumber?: string | null;
  depreciationAccountNumber?: string | null;
  accruedDepreciationAccountNumber?: string | null;
  assetCategoryName?: string | null;
  leaseAmount?: number | null;
  leaseContractSerialNumber?: string | null;
}

export class RouAssetListReportItem implements IRouAssetListReportItem {
  constructor(
    public id?: number,
    public modelTitle?: string | null,
    public modelVersion?: number | null,
    public description?: string | null,
    public leaseTermPeriods?: number | null,
    public rouModelReference?: string | null,
    public commencementDate?: dayjs.Dayjs | null,
    public expirationDate?: dayjs.Dayjs | null,
    public leaseContractTitle?: string | null,
    public assetAccountNumber?: string | null,
    public depreciationAccountNumber?: string | null,
    public accruedDepreciationAccountNumber?: string | null,
    public assetCategoryName?: string | null,
    public leaseAmount?: number | null,
    public leaseContractSerialNumber?: string | null
  ) {}
}

export function getRouAssetListReportItemIdentifier(rouAssetListReportItem: IRouAssetListReportItem): number | undefined {
  return rouAssetListReportItem.id;
}
