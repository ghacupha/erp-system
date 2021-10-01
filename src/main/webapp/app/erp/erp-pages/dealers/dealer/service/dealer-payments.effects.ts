import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  dealerPaymentCategoryRequisitionFailed,
  paymentCategoryAcquiredForPayment,
  requisitionForDealerCategoryInitiated
} from "../../../../store/actions/dealer-workflows-status.actions";
import {DealerCategoryService} from "./dealer-category.service";

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
