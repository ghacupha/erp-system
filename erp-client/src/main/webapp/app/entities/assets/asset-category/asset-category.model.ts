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
