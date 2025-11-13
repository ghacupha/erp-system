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
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';
import { ICardTypes } from 'app/entities/gdi/card-types/card-types.model';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { ICardClassType } from 'app/entities/gdi/card-class-type/card-class-type.model';
import { ICardCharges } from 'app/entities/gdi/card-charges/card-charges.model';

export interface ICardIssuerCharges {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  cardFeeChargeInLCY?: number;
  bankCode?: IInstitutionCode;
  cardCategory?: ICardCategoryType;
  cardType?: ICardTypes;
  cardBrand?: ICardBrandType;
  cardClass?: ICardClassType;
  cardChargeType?: ICardCharges;
}

export class CardIssuerCharges implements ICardIssuerCharges {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public cardFeeChargeInLCY?: number,
    public bankCode?: IInstitutionCode,
    public cardCategory?: ICardCategoryType,
    public cardType?: ICardTypes,
    public cardBrand?: ICardBrandType,
    public cardClass?: ICardClassType,
    public cardChargeType?: ICardCharges
  ) {}
}

export function getCardIssuerChargesIdentifier(cardIssuerCharges: ICardIssuerCharges): number | undefined {
  return cardIssuerCharges.id;
}
