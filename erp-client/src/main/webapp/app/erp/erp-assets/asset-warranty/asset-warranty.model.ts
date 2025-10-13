///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import * as dayjs from 'dayjs';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IBusinessDocument } from '../../erp-pages/business-document/business-document.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';

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
