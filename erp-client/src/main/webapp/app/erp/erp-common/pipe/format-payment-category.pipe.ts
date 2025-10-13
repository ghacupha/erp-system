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
import { IPaymentCategory } from '../../erp-settlements/payments/payment-category/payment-category.model';

@Pipe({
  name: 'formatPaymentCategory',
})
export class FormatPaymentCategoryPipe implements PipeTransform {

  transform(value: IPaymentCategory, args: any[]): string {

    let categoryDetail = '';


    if (value.categoryDescription) {

      const limit = args.length > 0 ? parseInt(args[0], 10) : 30;
      const trail = args.length > 1 ? args[1] : '...';

      let desc = '';

      if (value.categoryDescription.length > limit) {
        // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
         desc = value.categoryDescription.substring(0, limit) + trail;
      } else {
        desc = value.categoryDescription;
      }

      // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
      categoryDetail = `Id: ${value.id} | Name: ${value.categoryName} |${desc}`;
    }

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return value ? categoryDetail :'';
  }
}
