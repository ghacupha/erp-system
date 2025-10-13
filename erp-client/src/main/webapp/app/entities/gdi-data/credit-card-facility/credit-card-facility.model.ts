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
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { ICreditCardOwnership } from 'app/entities/gdi/credit-card-ownership/credit-card-ownership.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';

export interface ICreditCardFacility {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  totalNumberOfActiveCreditCards?: number;
  totalCreditCardLimitsInCCY?: number;
  totalCreditCardLimitsInLCY?: number;
  totalCreditCardAmountUtilisedInCCY?: number;
  totalCreditCardAmountUtilisedInLcy?: number;
  totalNPACreditCardAmountInFCY?: number;
  totalNPACreditCardAmountInLCY?: number;
  bankCode?: IInstitutionCode;
  customerCategory?: ICreditCardOwnership;
  currencyCode?: IIsoCurrencyCode;
}

export class CreditCardFacility implements ICreditCardFacility {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public totalNumberOfActiveCreditCards?: number,
    public totalCreditCardLimitsInCCY?: number,
    public totalCreditCardLimitsInLCY?: number,
    public totalCreditCardAmountUtilisedInCCY?: number,
    public totalCreditCardAmountUtilisedInLcy?: number,
    public totalNPACreditCardAmountInFCY?: number,
    public totalNPACreditCardAmountInLCY?: number,
    public bankCode?: IInstitutionCode,
    public customerCategory?: ICreditCardOwnership,
    public currencyCode?: IIsoCurrencyCode
  ) {}
}

export function getCreditCardFacilityIdentifier(creditCardFacility: ICreditCardFacility): number | undefined {
  return creditCardFacility.id;
}
