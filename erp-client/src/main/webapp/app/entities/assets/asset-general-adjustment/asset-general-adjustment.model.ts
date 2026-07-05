import * as dayjs from 'dayjs';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IAssetGeneralAdjustment {
  id?: number;
  description?: string | null;
  devaluationAmount?: number;
  adjustmentDate?: dayjs.Dayjs;
  timeOfCreation?: dayjs.Dayjs;
  adjustmentReferenceId?: string;
  effectivePeriod?: IDepreciationPeriod;
  assetRegistration?: IAssetRegistration;
  createdBy?: IApplicationUser | null;
  lastModifiedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
  placeholder?: IPlaceholder | null;
}

export class AssetGeneralAdjustment implements IAssetGeneralAdjustment {
  constructor(
    public id?: number,
    public description?: string | null,
    public devaluationAmount?: number,
    public adjustmentDate?: dayjs.Dayjs,
    public timeOfCreation?: dayjs.Dayjs,
    public adjustmentReferenceId?: string,
    public effectivePeriod?: IDepreciationPeriod,
    public assetRegistration?: IAssetRegistration,
    public createdBy?: IApplicationUser | null,
    public lastModifiedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null,
    public placeholder?: IPlaceholder | null
  ) {}
}

export function getAssetGeneralAdjustmentIdentifier(assetGeneralAdjustment: IAssetGeneralAdjustment): number | undefined {
  return assetGeneralAdjustment.id;
}
