import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {DealerCategoryService} from '../erp-pages/dealers/dealer/service/dealer-category.service';
import {
  dealerPaymentCategoryRequisitionFailed,
  paymentCategoryAcquiredForPayment, requisitionForDealerCategoryInitiated
} from './actions/dealer-workflows-status.actions';
import {catchError, map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';

@Injectable({ providedIn: 'root' })
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
