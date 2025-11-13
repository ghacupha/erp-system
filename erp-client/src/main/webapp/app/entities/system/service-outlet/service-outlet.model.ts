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
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';
import { ICountyCode } from 'app/entities/system/county-code/county-code.model';

export interface IServiceOutlet {
  id?: number;
  outletCode?: string;
  outletName?: string;
  town?: string | null;
  parliamentaryConstituency?: string | null;
  gpsCoordinates?: string | null;
  outletOpeningDate?: dayjs.Dayjs | null;
  regulatorApprovalDate?: dayjs.Dayjs | null;
  outletClosureDate?: dayjs.Dayjs | null;
  dateLastModified?: dayjs.Dayjs | null;
  licenseFeePayable?: number | null;
  placeholders?: IPlaceholder[] | null;
  bankCode?: IBankBranchCode | null;
  outletType?: IOutletType | null;
  outletStatus?: IOutletStatus | null;
  countyName?: ICountyCode | null;
  subCountyName?: ICountyCode | null;
}

export class ServiceOutlet implements IServiceOutlet {
  constructor(
    public id?: number,
    public outletCode?: string,
    public outletName?: string,
    public town?: string | null,
    public parliamentaryConstituency?: string | null,
    public gpsCoordinates?: string | null,
    public outletOpeningDate?: dayjs.Dayjs | null,
    public regulatorApprovalDate?: dayjs.Dayjs | null,
    public outletClosureDate?: dayjs.Dayjs | null,
    public dateLastModified?: dayjs.Dayjs | null,
    public licenseFeePayable?: number | null,
    public placeholders?: IPlaceholder[] | null,
    public bankCode?: IBankBranchCode | null,
    public outletType?: IOutletType | null,
    public outletStatus?: IOutletStatus | null,
    public countyName?: ICountyCode | null,
    public subCountyName?: ICountyCode | null
  ) {}
}

export function getServiceOutletIdentifier(serviceOutlet: IServiceOutlet): number | undefined {
  return serviceOutlet.id;
}
