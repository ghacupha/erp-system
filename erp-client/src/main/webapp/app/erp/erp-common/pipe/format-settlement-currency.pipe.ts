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

import { Pipe, PipeTransform } from '@angular/core';
import { ISettlementCurrency } from '../../erp-settlements/settlement-currency/settlement-currency.model';

@Pipe({
  name: 'formatSettlementCurrency',
})
export class FormatSettlementCurrencyPipe implements PipeTransform {

  transform(value: ISettlementCurrency, args: any[]): string {

    let detail = '';

    if (value.numericCode) {

      detail = `#: ${value.id} | ${value.currencyName} (${value.iso4217CurrencyCode}) ${value.country} | N-Code: ${value.numericCode}`;
    } else {
      detail = `#: ${value.id} | ${value.currencyName} (${value.iso4217CurrencyCode}) ${value.country}`;
    }

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return value ? detail :'';
  }
}
