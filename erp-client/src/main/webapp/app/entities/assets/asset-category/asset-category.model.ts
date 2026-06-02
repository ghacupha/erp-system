import { IDepreciationMethod } from 'app/entities/assets/depreciation-method/depreciation-method.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IAssetCategory {
  id?: number;
  assetCategoryName?: string;
  description?: string | null;
  notes?: string | null;
  remarks?: string | null;
  depreciationRateYearly?: number | null;
  depreciationMethod?: IDepreciationMethod;
  placeholders?: IPlaceholder[] | null;
}

export class AssetCategory implements IAssetCategory {
  constructor(
    public id?: number,
    public assetCategoryName?: string,
    public description?: string | null,
    public notes?: string | null,
    public remarks?: string | null,
    public depreciationRateYearly?: number | null,
    public depreciationMethod?: IDepreciationMethod,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getAssetCategoryIdentifier(assetCategory: IAssetCategory): number | undefined {
  return assetCategory.id;
}
