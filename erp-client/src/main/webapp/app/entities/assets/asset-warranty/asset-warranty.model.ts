import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';

export interface IAssetWarranty {
  id?: number;
  assetTag?: string | null;
  description?: string | null;
  modelNumber?: string | null;
  serialNumber?: string | null;
  expiryDate?: dayjs.Dayjs | null;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  dealer?: IDealer;
  warrantyAttachments?: IBusinessDocument[] | null;
}

export class AssetWarranty implements IAssetWarranty {
  constructor(
    public id?: number,
    public assetTag?: string | null,
    public description?: string | null,
    public modelNumber?: string | null,
    public serialNumber?: string | null,
    public expiryDate?: dayjs.Dayjs | null,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public dealer?: IDealer,
    public warrantyAttachments?: IBusinessDocument[] | null
  ) {}
}

export function getAssetWarrantyIdentifier(assetWarranty: IAssetWarranty): number | undefined {
  return assetWarranty.id;
}
