import { Moment } from 'moment';
import { DepreciationRegime } from 'app/shared/model/enumerations/depreciation-regime.model';

export interface IFixedAssetNetBookValue {
  id?: number;
  assetNumber?: number;
  serviceOutletCode?: string;
  assetTag?: string;
  assetDescription?: string;
  netBookValueDate?: Moment;
  assetCategory?: string;
  netBookValue?: number;
  depreciationRegime?: DepreciationRegime;
  fileUploadToken?: string;
  compilationToken?: string;
}

export class FixedAssetNetBookValue implements IFixedAssetNetBookValue {
  constructor(
    public id?: number,
    public assetNumber?: number,
    public serviceOutletCode?: string,
    public assetTag?: string,
    public assetDescription?: string,
    public netBookValueDate?: Moment,
    public assetCategory?: string,
    public netBookValue?: number,
    public depreciationRegime?: DepreciationRegime,
    public fileUploadToken?: string,
    public compilationToken?: string
  ) {}
}
