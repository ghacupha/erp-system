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

import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IOutletType {
  id?: number;
  outletTypeCode?: string;
  outletType?: string;
  outletTypeDetails?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class OutletType implements IOutletType {
  constructor(
    public id?: number,
    public outletTypeCode?: string,
    public outletType?: string,
    public outletTypeDetails?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getOutletTypeIdentifier(outletType: IOutletType): number | undefined {
  return outletType.id;
}
