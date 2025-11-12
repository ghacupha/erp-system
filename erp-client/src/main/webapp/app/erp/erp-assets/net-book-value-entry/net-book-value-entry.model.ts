///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { IAssetRegistration } from '../asset-registration/asset-registration.model';
import { IAssetCategory } from '../asset-category/asset-category.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IFiscalMonth } from '../../erp-pages/fiscal-month/fiscal-month.model';
import { IDepreciationMethod } from '../depreciation-method/depreciation-method.model';
import { IDepreciationPeriod } from '../depreciation-period/depreciation-period.model';
import { IServiceOutlet } from '../../erp-granular/service-outlet/service-outlet.model';

export interface INetBookValueEntry {
  id?: number;
  assetNumber?: string | null;
  assetTag?: string | null;
  assetDescription?: string | null;
  nbvIdentifier?: string;
  compilationJobIdentifier?: string | null;
  compilationBatchIdentifier?: string | null;
  elapsedMonths?: number | null;
  priorMonths?: number | null;
  usefulLifeYears?: number | null;
  netBookValueAmount?: number | null;
  previousNetBookValueAmount?: number | null;
  historicalCost?: number | null;
  serviceOutlet?: IServiceOutlet | null;
  depreciationPeriod?: IDepreciationPeriod | null;
  fiscalMonth?: IFiscalMonth | null;
  depreciationMethod?: IDepreciationMethod | null;
  assetRegistration?: IAssetRegistration | null;
  assetCategory?: IAssetCategory | null;
  placeholders?: IPlaceholder[] | null;
}

export class NetBookValueEntry implements INetBookValueEntry {
  constructor(
    public id?: number,
    public assetNumber?: string | null,
    public assetTag?: string | null,
    public assetDescription?: string | null,
    public nbvIdentifier?: string,
    public compilationJobIdentifier?: string | null,
    public compilationBatchIdentifier?: string | null,
    public elapsedMonths?: number | null,
    public priorMonths?: number | null,
    public usefulLifeYears?: number | null,
    public netBookValueAmount?: number | null,
    public previousNetBookValueAmount?: number | null,
    public historicalCost?: number | null,
    public serviceOutlet?: IServiceOutlet | null,
    public depreciationPeriod?: IDepreciationPeriod | null,
    public fiscalMonth?: IFiscalMonth | null,
    public depreciationMethod?: IDepreciationMethod | null,
    public assetRegistration?: IAssetRegistration | null,
    public assetCategory?: IAssetCategory | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getNetBookValueEntryIdentifier(netBookValueEntry: INetBookValueEntry): number | undefined {
  return netBookValueEntry.id;
}
