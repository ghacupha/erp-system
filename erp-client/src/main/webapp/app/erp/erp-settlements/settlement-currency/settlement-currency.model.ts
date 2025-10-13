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

import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export interface ISettlementCurrency {
  id?: number;
  iso4217CurrencyCode?: string;
  currencyName?: string;
  country?: string;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  placeholders?: IPlaceholder[] | null;
  numericCode?: string
}

export class SettlementCurrency implements ISettlementCurrency {
  constructor(
    public id?: number,
    public iso4217CurrencyCode?: string,
    public currencyName?: string,
    public country?: string,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public placeholders?: IPlaceholder[] | null,
    public numericCode?: string
  ) {}
}

export function getSettlementCurrencyIdentifier(settlementCurrency: ISettlementCurrency): number | undefined {
  return settlementCurrency.id;
}
