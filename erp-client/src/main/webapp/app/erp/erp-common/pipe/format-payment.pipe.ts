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

import { Pipe, PipeTransform } from '@angular/core';
import { IPayment } from '../../erp-pages/payments/payment/payment.model';

@Pipe({
  name: 'formatPayment',
})
export class FormatPaymentPipe implements PipeTransform {

  transform(value: IPayment, args: any[]): string {

    let detail = '';


    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (value) {

      const limit = args.length > 0 ? parseInt(args[0], 10) : 30;
      const trail = args.length > 1 ? args[1] : '...';

      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands,@typescript-eslint/ban-ts-comment
      // @ts-ignore
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      const desc = value.description!.length > limit ? value.description.substing(0, limit) + trail : value.description;

      // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
      detail = `id: ${value.id} | #: ${value.paymentNumber} | dd: ${value.paymentDate} |${desc}`;
    }

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return value ? detail :'';
  }
}
