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

export interface IInstitutionCode {
  id?: number;
  institutionCode?: string;
  institutionName?: string;
  shortName?: string | null;
  category?: string | null;
  institutionCategory?: string | null;
  institutionOwnership?: string | null;
  dateLicensed?: dayjs.Dayjs | null;
  institutionStatus?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class InstitutionCode implements IInstitutionCode {
  constructor(
    public id?: number,
    public institutionCode?: string,
    public institutionName?: string,
    public shortName?: string | null,
    public category?: string | null,
    public institutionCategory?: string | null,
    public institutionOwnership?: string | null,
    public dateLicensed?: dayjs.Dayjs | null,
    public institutionStatus?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getInstitutionCodeIdentifier(institutionCode: IInstitutionCode): number | undefined {
  return institutionCode.id;
}
