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

export const dealerAcquiredForPayment = createAction(
  '[Dealers page] dealer acquired for payment',
  props<{selectedDealer: IDealer}>()
);

export const requisitionForDealerCategoryInitiated = createAction(
  '[Dealers page] requisition for dealer-category initiated',
  props<{selectedDealer: IDealer}>()
);

export const paymentCategoryAcquiredForPayment = createAction(
  '[Dealer-payments-effects] payment category acquired for payment',
  props<{paymentDealerCategory: IPaymentCategory}>()
);

export const dealerPaymentCategoryRequisitionFailed = createAction(
  '[Dealer-payments-effects] dealer payment category',
  props<{error: string}>()
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
