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

import { CardCategoryFlag } from 'app/entities/enumerations/card-category-flag.model';

export interface ICardCategoryType {
  id?: number;
  cardCategoryFlag?: CardCategoryFlag;
  cardCategoryDescription?: string;
  cardCategoryDetails?: string | null;
}

export class CardCategoryType implements ICardCategoryType {
  constructor(
    public id?: number,
    public cardCategoryFlag?: CardCategoryFlag,
    public cardCategoryDescription?: string,
    public cardCategoryDetails?: string | null
  ) {}
}

export function getCardCategoryTypeIdentifier(cardCategoryType: ICardCategoryType): number | undefined {
  return cardCategoryType.id;
}
