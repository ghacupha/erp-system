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

import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  dealerPaymentCategoryRequisitionFailed,
  paymentCategoryAcquiredForPayment,
  requisitionForDealerCategoryInitiated
} from "../actions/dealer-workflows-status.actions";
import {DealerCategoryService} from "../../erp-pages/dealers/dealer/service/dealer-category.service";

@Injectable()
export class DealerPaymentsEffects {

  paymentCategoryEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(requisitionForDealerCategoryInitiated),
      switchMap(action =>
        this.dealerCategoryService.getDealerCategory(action.selectedDealer).pipe(
          map(paymentDealerCategory => paymentCategoryAcquiredForPayment({paymentDealerCategory})),
          catchError(err => of(dealerPaymentCategoryRequisitionFailed({error: err})))
        )
      )
      )
  );

  constructor(
    protected actions$: Actions,
    protected dealerCategoryService: DealerCategoryService) {
  }
}
