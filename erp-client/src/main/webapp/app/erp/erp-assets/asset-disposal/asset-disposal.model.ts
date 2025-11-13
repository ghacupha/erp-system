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
import { IAssetRegistration } from '../asset-registration/asset-registration.model';
import { IDepreciationPeriod } from '../depreciation-period/depreciation-period.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';

export interface IAssetDisposal {
  id?: number;
  assetDisposalReference?: string | null;
  description?: string | null;
  assetCost?: number | null;
  historicalCost?: number | null;
  accruedDepreciation?: number;
  netBookValue?: number;
  decommissioningDate?: dayjs.Dayjs | null;
  disposalDate?: dayjs.Dayjs;
  dormant?: boolean | null;
  createdBy?: IApplicationUser | null;
  modifiedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
  effectivePeriod?: IDepreciationPeriod;
  placeholders?: IPlaceholder[] | null;
  assetDisposed?: IAssetRegistration;
}

export class AssetDisposal implements IAssetDisposal {
  constructor(
    public id?: number,
    public assetDisposalReference?: string | null,
    public description?: string | null,
    public assetCost?: number | null,
    public historicalCost?: number | null,
    public accruedDepreciation?: number,
    public netBookValue?: number,
    public decommissioningDate?: dayjs.Dayjs | null,
    public disposalDate?: dayjs.Dayjs,
    public dormant?: boolean | null,
    public createdBy?: IApplicationUser | null,
    public modifiedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null,
    public effectivePeriod?: IDepreciationPeriod,
    public placeholders?: IPlaceholder[] | null,
    public assetDisposed?: IAssetRegistration
  ) {
    this.dormant = this.dormant ?? false;
  }
}

export function getAssetDisposalIdentifier(assetDisposal: IAssetDisposal): number | undefined {
  return assetDisposal.id;
}
