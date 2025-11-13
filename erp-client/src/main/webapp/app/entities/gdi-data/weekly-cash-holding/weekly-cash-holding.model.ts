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
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { IKenyanCurrencyDenomination } from 'app/entities/gdi/kenyan-currency-denomination/kenyan-currency-denomination.model';

export interface IWeeklyCashHolding {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  fitUnits?: number;
  unfitUnits?: number;
  bankCode?: IInstitutionCode;
  branchId?: IBankBranchCode;
  subCountyCode?: ICountySubCountyCode;
  denomination?: IKenyanCurrencyDenomination;
}

export class WeeklyCashHolding implements IWeeklyCashHolding {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public fitUnits?: number,
    public unfitUnits?: number,
    public bankCode?: IInstitutionCode,
    public branchId?: IBankBranchCode,
    public subCountyCode?: ICountySubCountyCode,
    public denomination?: IKenyanCurrencyDenomination
  ) {}
}

export function getWeeklyCashHoldingIdentifier(weeklyCashHolding: IWeeklyCashHolding): number | undefined {
  return weeklyCashHolding.id;
}
