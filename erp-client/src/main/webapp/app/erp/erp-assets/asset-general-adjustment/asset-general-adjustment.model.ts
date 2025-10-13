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
import { IDepreciationPeriod } from '../depreciation-period/depreciation-period.model';
import { IAssetRegistration } from '../asset-registration/asset-registration.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';

export interface IAssetGeneralAdjustment {
  id?: number;
  description?: string | null;
  devaluationAmount?: number;
  adjustmentDate?: dayjs.Dayjs;
  timeOfCreation?: dayjs.Dayjs;
  adjustmentReferenceId?: string;
  effectivePeriod?: IDepreciationPeriod;
  assetRegistration?: IAssetRegistration;
  createdBy?: IApplicationUser | null;
  lastModifiedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
  placeholder?: IPlaceholder | null;
}

export class AssetGeneralAdjustment implements IAssetGeneralAdjustment {
  constructor(
    public id?: number,
    public description?: string | null,
    public devaluationAmount?: number,
    public adjustmentDate?: dayjs.Dayjs,
    public timeOfCreation?: dayjs.Dayjs,
    public adjustmentReferenceId?: string,
    public effectivePeriod?: IDepreciationPeriod,
    public assetRegistration?: IAssetRegistration,
    public createdBy?: IApplicationUser | null,
    public lastModifiedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null,
    public placeholder?: IPlaceholder | null
  ) {}
}

export function getAssetGeneralAdjustmentIdentifier(assetGeneralAdjustment: IAssetGeneralAdjustment): number | undefined {
  return assetGeneralAdjustment.id;
}
