///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

export interface IFixedAssetAcquisition {
  id?: number;
  assetNumber?: number | null;
  serviceOutletCode?: string | null;
  assetTag?: string | null;
  assetDescription?: string | null;
  purchaseDate?: dayjs.Dayjs | null;
  assetCategory?: string | null;
  purchasePrice?: number | null;
  fileUploadToken?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class FixedAssetAcquisition implements IFixedAssetAcquisition {
  constructor(
    public id?: number,
    public assetNumber?: number | null,
    public serviceOutletCode?: string | null,
    public assetTag?: string | null,
    public assetDescription?: string | null,
    public purchaseDate?: dayjs.Dayjs | null,
    public assetCategory?: string | null,
    public purchasePrice?: number | null,
    public fileUploadToken?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getFixedAssetAcquisitionIdentifier(fixedAssetAcquisition: IFixedAssetAcquisition): number | undefined {
  return fixedAssetAcquisition.id;
}
