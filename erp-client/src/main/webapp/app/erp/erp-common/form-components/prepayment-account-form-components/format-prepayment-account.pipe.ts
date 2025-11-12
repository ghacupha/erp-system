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
import { IPrepaymentAccount } from '../../../erp-prepayments/prepayment-account/prepayment-account.model';
import { formatCurrency } from '@angular/common';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';

@Pipe({
  name: 'formatPrepaymentAccount',
})
export class FormatPrepaymentAccountPipe implements PipeTransform {

  transform(value: IPrepaymentAccount): string {

    let accountDetail = '';

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (value) {

      let prepaidAmount;

      if (typeof value.prepaymentAmount === 'number') {
        prepaidAmount = formatCurrency(value.prepaymentAmount, 'en', 'CU ');
      }

      const prepaidDate  = value.recognitionDate?.format('DD/MM/YY');

      accountDetail = `${value.catalogueNumber}-${value.particulars} ${prepaidAmount} On: ${prepaidDate}`;

      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dealerDetails: IDealer = <IDealer>value.dealer

      // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
      if (dealerDetails.dealerName) {
        const dealerName = dealerDetails.dealerName;
        // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
        accountDetail = `${value.catalogueNumber}-${value.particulars} ${prepaidAmount} On: ${prepaidDate} ${dealerName}`;
      }
    }

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return value ? accountDetail :'';
  }
}
