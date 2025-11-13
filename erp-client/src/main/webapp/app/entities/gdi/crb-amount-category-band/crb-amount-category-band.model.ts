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

export interface ICrbAmountCategoryBand {
  id?: number;
  amountCategoryBandCode?: string;
  amountCategoryBand?: string;
  amountCategoryBandDetails?: string | null;
}

export class CrbAmountCategoryBand implements ICrbAmountCategoryBand {
  constructor(
    public id?: number,
    public amountCategoryBandCode?: string,
    public amountCategoryBand?: string,
    public amountCategoryBandDetails?: string | null
  ) {}
}

export function getCrbAmountCategoryBandIdentifier(crbAmountCategoryBand: ICrbAmountCategoryBand): number | undefined {
  return crbAmountCategoryBand.id;
}
