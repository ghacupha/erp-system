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
import { IPlaceholder } from '../placeholder/placeholder.model';
import { IApplicationUser } from '../application-user/application-user.model';
import { FiscalYearStatusType } from '../../erp-common/enumerations/fiscal-year-status-type.model';
import { IUniversallyUniqueMapping } from '../universally-unique-mapping/universally-unique-mapping.model';

export interface IFiscalYear {
  id?: number;
  fiscalYearCode?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  fiscalYearStatus?: FiscalYearStatusType | null;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  createdBy?: IApplicationUser | null;
  lastUpdatedBy?: IApplicationUser | null;
}

export class FiscalYear implements IFiscalYear {
  constructor(
    public id?: number,
    public fiscalYearCode?: string,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public fiscalYearStatus?: FiscalYearStatusType | null,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public createdBy?: IApplicationUser | null,
    public lastUpdatedBy?: IApplicationUser | null
  ) {}
}

export function getFiscalYearIdentifier(fiscalYear: IFiscalYear): number | undefined {
  return fiscalYear.id;
}
