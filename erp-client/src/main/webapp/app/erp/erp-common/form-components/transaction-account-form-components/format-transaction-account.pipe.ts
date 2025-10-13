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
import { ITransactionAccount } from '../../../erp-accounts/transaction-account/transaction-account.model';

@Pipe({
  name: 'formatTransactionAccount',
})
export class FormatTransactionAccountPipe implements PipeTransform {

  transform(value: ITransactionAccount, args: any[]): string {

    let detail = '';

    if (value.accountName) {

      const limit = args.length > 0 ? parseInt(args[0], 10) : 30;
      const trail = args.length > 1 ? args[1] : '...';

      let desc = '';

      if (value.accountName.length > limit) {
        // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
        desc = value.accountName.substring(0, limit) + trail;
      } else {
        desc = value.accountName;
      }

      if (value.accountNumber) {
        // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
        detail = `id: ${value.id} | #: ${value.accountNumber} | ${desc}`;
      } else {
        detail = `id: ${value.id} | Account Name ${desc}`;
      }
    }

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return value ? detail :'';
  }
}
