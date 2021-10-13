import {createAction, props} from "@ngrx/store";
import {IDealer} from "../../erp-pages/dealers/dealer/dealer.model";
import {IInvoice} from "../../erp-pages/payments/invoice/invoice.model";
import {IPaymentLabel} from "../../erp-pages/payment-label/payment-label.model";
import {IPlaceholder} from "../../../entities/erpService/placeholder/placeholder.model";

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

export const dealerAcquiredForInvoicedPayment = createAction(
  '[Dealer-Invoice-payment-resolve-service] dealer acquired for invoiced payment',
  props<{
    paymentLabels: IPaymentLabel[],
    placeholders: IPlaceholder[],
  }>()
);

export const invoiceAcquiredForPaymentWithLabels = createAction(
  '[Invoice-List] invoice acquired for payment with labels',
  props<{paymentLabels: IPaymentLabel[]}>()
);

export const invoiceAcquiredForPaymentWithPlaceholders = createAction(
  '[Invoice-List] invoice acquired for payment with placeholders',
  props<{placeholders: IPlaceholder[]}>()
);

export const dealerInvoiceStateReset = createAction(
  '[System] dealer invoice state has been reset');
