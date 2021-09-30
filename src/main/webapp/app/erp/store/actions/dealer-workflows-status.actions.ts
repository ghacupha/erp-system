import {createAction, props} from '@ngrx/store';
import {IDealer} from '../../erp-pages/dealers/dealer/dealer.model';
import {IPaymentCategory} from '../../erp-pages/payments/payment-category/payment-category.model';

export const payDealerButtonClicked = createAction(
  '[Dealers Payments Page] pay dealers button clicked',
  props<{selectedDealer: IDealer}>()
);

export const paymentToDealerCompleted = createAction(
  '[Payment Update form pay button] payment to dealer completed',
);

export const paymentDealerCategoryAcquired = createAction(
  '[Dealer-Category-Service] payment dealer category acquired',
  props<{paymentDealerCategory: IPaymentCategory}>()
);
