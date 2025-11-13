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
import { IChannelType } from 'app/entities/gdi/channel-type/channel-type.model';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';

export interface ICardAcquiringTransaction {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  terminalId?: string;
  numberOfTransactions?: number;
  valueOfTransactionsInLCY?: number;
  bankCode?: IInstitutionCode;
  channelType?: IChannelType;
  cardBrandType?: ICardBrandType;
  currencyOfTransaction?: IIsoCurrencyCode;
  cardIssuerCategory?: ICardCategoryType;
}

export class CardAcquiringTransaction implements ICardAcquiringTransaction {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public terminalId?: string,
    public numberOfTransactions?: number,
    public valueOfTransactionsInLCY?: number,
    public bankCode?: IInstitutionCode,
    public channelType?: IChannelType,
    public cardBrandType?: ICardBrandType,
    public currencyOfTransaction?: IIsoCurrencyCode,
    public cardIssuerCategory?: ICardCategoryType
  ) {}
}

export function getCardAcquiringTransactionIdentifier(cardAcquiringTransaction: ICardAcquiringTransaction): number | undefined {
  return cardAcquiringTransaction.id;
}
