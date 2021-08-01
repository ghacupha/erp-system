import * as dayjs from 'dayjs';
import { DepreciationRegime } from 'app/entities/enumerations/depreciation-regime.model';

export interface IFixedAssetDepreciation {
  id?: number;
  assetNumber?: number | null;
  serviceOutletCode?: string | null;
  assetTag?: string | null;
  assetDescription?: string | null;
  depreciationDate?: dayjs.Dayjs | null;
  assetCategory?: string | null;
  depreciationAmount?: number | null;
  depreciationRegime?: DepreciationRegime | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
}

export class FixedAssetDepreciation implements IFixedAssetDepreciation {
  constructor(
    public id?: number,
    public assetNumber?: number | null,
    public serviceOutletCode?: string | null,
    public assetTag?: string | null,
    public assetDescription?: string | null,
    public depreciationDate?: dayjs.Dayjs | null,
    public assetCategory?: string | null,
    public depreciationAmount?: number | null,
    public depreciationRegime?: DepreciationRegime | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null
  ) {}
}

export function getFixedAssetDepreciationIdentifier(fixedAssetDepreciation: IFixedAssetDepreciation): number | undefined {
  return fixedAssetDepreciation.id;
}
