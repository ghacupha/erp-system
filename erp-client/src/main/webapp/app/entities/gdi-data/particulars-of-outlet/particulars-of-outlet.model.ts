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
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';

export interface IParticularsOfOutlet {
  id?: number;
  businessReportingDate?: dayjs.Dayjs;
  outletName?: string;
  town?: string;
  iso6709Latitute?: number;
  iso6709Longitude?: number;
  cbkApprovalDate?: dayjs.Dayjs;
  outletOpeningDate?: dayjs.Dayjs;
  outletClosureDate?: dayjs.Dayjs | null;
  licenseFeePayable?: number;
  subCountyCode?: ICountySubCountyCode;
  bankCode?: IInstitutionCode;
  outletId?: IBankBranchCode;
  typeOfOutlet?: IOutletType;
  outletStatus?: IOutletStatus;
}

export class ParticularsOfOutlet implements IParticularsOfOutlet {
  constructor(
    public id?: number,
    public businessReportingDate?: dayjs.Dayjs,
    public outletName?: string,
    public town?: string,
    public iso6709Latitute?: number,
    public iso6709Longitude?: number,
    public cbkApprovalDate?: dayjs.Dayjs,
    public outletOpeningDate?: dayjs.Dayjs,
    public outletClosureDate?: dayjs.Dayjs | null,
    public licenseFeePayable?: number,
    public subCountyCode?: ICountySubCountyCode,
    public bankCode?: IInstitutionCode,
    public outletId?: IBankBranchCode,
    public typeOfOutlet?: IOutletType,
    public outletStatus?: IOutletStatus
  ) {}
}

export function getParticularsOfOutletIdentifier(particularsOfOutlet: IParticularsOfOutlet): number | undefined {
  return particularsOfOutlet.id;
}
