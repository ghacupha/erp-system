import { Moment } from 'moment';
import { DepreciationRegime } from 'app/shared/model/enumerations/depreciation-regime.model';

export interface IFixedAssetDepreciation {
  id?: number;
  assetNumber?: number;
  serviceOutletCode?: string;
  assetTag?: string;
  assetDescription?: string;
  depreciationDate?: Moment;
  assetCategory?: string;
  depreciationAmount?: number;
  depreciationRegime?: DepreciationRegime;
  fileUploadToken?: string;
  compilationToken?: string;
}

export class FixedAssetDepreciation implements IFixedAssetDepreciation {
  constructor(
    public id?: number,
    public assetNumber?: number,
    public serviceOutletCode?: string,
    public assetTag?: string,
    public assetDescription?: string,
    public depreciationDate?: Moment,
    public assetCategory?: string,
    public depreciationAmount?: number,
    public depreciationRegime?: DepreciationRegime,
    public fileUploadToken?: string,
    public compilationToken?: string
  ) {}
}
