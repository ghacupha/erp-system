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

import * as dayjs from 'dayjs';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';

export interface IAssetWriteOff {
  id?: number;
  description?: string | null;
  writeOffAmount?: number;
  writeOffDate?: dayjs.Dayjs;
  writeOffReferenceId?: string | null;
  createdBy?: IApplicationUser | null;
  modifiedBy?: IApplicationUser | null;
  lastAccessedBy?: IApplicationUser | null;
  effectivePeriod?: IDepreciationPeriod;
  placeholders?: IPlaceholder[] | null;
  assetWrittenOff?: IAssetRegistration;
}

export class AssetWriteOff implements IAssetWriteOff {
  constructor(
    public id?: number,
    public description?: string | null,
    public writeOffAmount?: number,
    public writeOffDate?: dayjs.Dayjs,
    public writeOffReferenceId?: string | null,
    public createdBy?: IApplicationUser | null,
    public modifiedBy?: IApplicationUser | null,
    public lastAccessedBy?: IApplicationUser | null,
    public effectivePeriod?: IDepreciationPeriod,
    public placeholders?: IPlaceholder[] | null,
    public assetWrittenOff?: IAssetRegistration
  ) {}
}

export function getAssetWriteOffIdentifier(assetWriteOff: IAssetWriteOff): number | undefined {
  return assetWriteOff.id;
}
