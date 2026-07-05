import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';

export interface IAssetDisposal {
  id?: number;
  assetDisposalReference?: string | null;
  description?: string | null;
  assetCost?: number | null;
  historicalCost?: number | null;
  accruedDepreciation?: number;
  netBookValue?: number;
  decommissioningDate?: dayjs.Dayjs | null;
  disposalDate?: dayjs.Dayjs;
  dormant?: boolean | null;
  createdBy?: IApplicationUser | null;
  modifiedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
  effectivePeriod?: IDepreciationPeriod;
  placeholders?: IPlaceholder[] | null;
  assetDisposed?: IAssetRegistration;
}

export class AssetDisposal implements IAssetDisposal {
  constructor(
    public id?: number,
    public assetDisposalReference?: string | null,
    public description?: string | null,
    public assetCost?: number | null,
    public historicalCost?: number | null,
    public accruedDepreciation?: number,
    public netBookValue?: number,
    public decommissioningDate?: dayjs.Dayjs | null,
    public disposalDate?: dayjs.Dayjs,
    public dormant?: boolean | null,
    public createdBy?: IApplicationUser | null,
    public modifiedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null,
    public effectivePeriod?: IDepreciationPeriod,
    public placeholders?: IPlaceholder[] | null,
    public assetDisposed?: IAssetRegistration
  ) {
    this.dormant = this.dormant ?? false;
  }
}

export function getAssetDisposalIdentifier(assetDisposal: IAssetDisposal): number | undefined {
  return assetDisposal.id;
}
