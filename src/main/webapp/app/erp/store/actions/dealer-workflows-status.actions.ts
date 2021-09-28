import {createAction, props} from "@ngrx/store";
import {IDealer} from "../../erp-pages/dealers/dealer/dealer.model";
import {IPayment} from "../../erp-pages/payments/payment/payment.model";

export const payDealerButtonClicked = createAction(
  '[Dealers Payments Page] pay dealers button clicked',
  props<{selectedDealer: IDealer}>()
);

export const paymentToDealerInitiated = createAction(
  '[Dealer Payment Resolve Service] payment to dealer initiated',
  props<{dealerPayment: IPayment}>()
);

export const paymentToDealerCompleted = createAction(
  '[Payment Update form pay button] payment to dealer completed',
);
