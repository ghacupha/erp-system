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

import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';

export interface IExchangeRate {
  id?: number;
  businessReportingDay?: dayjs.Dayjs;
  buyingRate?: number;
  sellingRate?: number;
  meanRate?: number;
  closingBidRate?: number;
  closingOfferRate?: number;
  usdCrossRate?: number;
  institutionCode?: IInstitutionCode;
  currencyCode?: IIsoCurrencyCode;
}

export class ExchangeRate implements IExchangeRate {
  constructor(
    public id?: number,
    public businessReportingDay?: dayjs.Dayjs,
    public buyingRate?: number,
    public sellingRate?: number,
    public meanRate?: number,
    public closingBidRate?: number,
    public closingOfferRate?: number,
    public usdCrossRate?: number,
    public institutionCode?: IInstitutionCode,
    public currencyCode?: IIsoCurrencyCode
  ) {}
}

export function getExchangeRateIdentifier(exchangeRate: IExchangeRate): number | undefined {
  return exchangeRate.id;
}
