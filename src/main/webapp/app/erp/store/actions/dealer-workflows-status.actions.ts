import {createAction, props} from '@ngrx/store';
import {IDealer} from '../../erp-pages/dealers/dealer/dealer.model';
import {IPaymentCategory} from '../../erp-pages/payments/payment-category/payment-category.model';

export const payDealerButtonClicked = createAction(
  '[Dealer-category-service] pay dealers button clicked',
  props<{
    selectedDealer: IDealer,
    paymentDealerCategory: IPaymentCategory
  }>()
);

export const paymentDealerCategoryAcquired = createAction(
  '[Dealer-Category-Service] payment dealer category acquired',
  props<{paymentDealerCategory: IPaymentCategory}>()
);

export const paymentToDealerReset = createAction(
  '[Payment-Update-Form] payment details with dealer reset',
);

export const paymentToDealerCompleted = createAction(
  '[Payment-Update-Form pay button] payment details with dealer completed',
);
