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
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';

export interface IFiscalMonth {
  id?: number;
  monthNumber?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  fiscalMonthCode?: string;
  fiscalYear?: IFiscalYear;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  fiscalQuarter?: IFiscalQuarter | null;
}

export class FiscalMonth implements IFiscalMonth {
  constructor(
    public id?: number,
    public monthNumber?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public fiscalMonthCode?: string,
    public fiscalYear?: IFiscalYear,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public fiscalQuarter?: IFiscalQuarter | null
  ) {}
}

export function getFiscalMonthIdentifier(fiscalMonth: IFiscalMonth): number | undefined {
  return fiscalMonth.id;
}
