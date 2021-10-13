import {createAction, props} from "@ngrx/store";
import {IDealer} from "../../erp-pages/dealers/dealer/dealer.model";
import {IInvoice} from "../../erp-pages/payments/invoice/invoice.model";

export const recordDealerInvoiceButtonClicked = createAction(
  '[Dealers Page] record dealer invoice button clicked',
  props<{
    selectedDealer: IDealer,
  }>()
);

export const recordInvoicePaymentButtonClicked = createAction(
  '[Invoice Update Form] record invoice payment button clicked',
  props<{
    selectedInvoice: IInvoice,
  }>()
);

export const addPaymentToInvoiceButtonClicked = createAction(
  '[Invoice List] add payment to invoice button clicked',
  props<{
    selectedInvoice: IInvoice,
    selectedDealer: IDealer,
  }>()
);

export const dealerInvoiceStateReset = createAction(
  '[System] dealer invoice state has been reset');
