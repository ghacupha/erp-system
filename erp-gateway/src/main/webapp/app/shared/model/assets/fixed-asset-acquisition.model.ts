import { Moment } from 'moment';

export interface IFixedAssetAcquisition {
  id?: number;
  assetNumber?: number;
  serviceOutletCode?: string;
  assetTag?: string;
  assetDescription?: string;
  purchaseDate?: Moment;
  assetCategory?: string;
  purchasePrice?: number;
  fileUploadToken?: string;
}

export class FixedAssetAcquisition implements IFixedAssetAcquisition {
  constructor(
    public id?: number,
    public assetNumber?: number,
    public serviceOutletCode?: string,
    public assetTag?: string,
    public assetDescription?: string,
    public purchaseDate?: Moment,
    public assetCategory?: string,
    public purchasePrice?: number,
    public fileUploadToken?: string
  ) {}
}
