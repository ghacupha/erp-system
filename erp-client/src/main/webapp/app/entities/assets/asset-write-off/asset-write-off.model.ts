import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';

export interface IAssetWriteOff {
  id?: number;
  description?: string | null;
  writeOffAmount?: number;
  writeOffDate?: dayjs.Dayjs;
  writeOffReferenceId?: string | null;
  createdBy?: IApplicationUser | null;
  modifiedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
  effectivePeriod?: IDepreciationPeriod;
  placeholders?: IPlaceholder[] | null;
  assetWrittenOff?: IAssetRegistration;
}

export class AssetWriteOff implements IAssetWriteOff {
  constructor(
    public id?: number,
    public description?: string | null,
    public writeOffAmount?: number,
    public writeOffDate?: dayjs.Dayjs,
    public writeOffReferenceId?: string | null,
    public createdBy?: IApplicationUser | null,
    public modifiedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null,
    public effectivePeriod?: IDepreciationPeriod,
    public placeholders?: IPlaceholder[] | null,
    public assetWrittenOff?: IAssetRegistration
  ) {}
}

export function getAssetWriteOffIdentifier(assetWriteOff: IAssetWriteOff): number | undefined {
  return assetWriteOff.id;
}
