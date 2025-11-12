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

import { Injectable } from '@angular/core';
import { IPaymentCategory } from '../../payments/payment-category/payment-category.model';
import { Observable, of } from 'rxjs';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { CategoryTypes } from '../../../erp-common/enumerations/category-types.model';

@Injectable({ providedIn: 'root' })
export class PaymentCalculatorService {

  constructor(protected universallyUniqueMappingService: UniversallyUniqueMappingService) {
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  calculatePayableAmount(settlementAmount: number, category: IPaymentCategory): Observable<number> {

    const VAT_RATE = 0.16;
    const WITHHOLDING_VAT_RATE = 0.02
    const CONSULTANCY_WITHHOLDING_TAX = 0.05;

    if (category.categoryType === CategoryTypes.CATEGORY2) {
      return of(
        Math.round(
        settlementAmount - (
          (settlementAmount/(1 + VAT_RATE) * WITHHOLDING_VAT_RATE) +
          (settlementAmount/(1 + VAT_RATE) * CONSULTANCY_WITHHOLDING_TAX)
        ))
      );
    }

    if (category.categoryType === CategoryTypes.CATEGORY0) {
      return of(Math.round(settlementAmount));
    }


    if (category.categoryType === CategoryTypes.CATEGORY1) {
      return of(Math.round(settlementAmount - (settlementAmount/(1 + VAT_RATE) * WITHHOLDING_VAT_RATE)));
    }

    return of(0);
  }
}
