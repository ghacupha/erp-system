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

export const dealerInvoicePaymentWorkflowCancelled = createAction(
  '[Invoice Update: Cancel BUtton] dealerInvoicePaymentWorkflowCancelled',
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

export const paymentToInvoiceDealerConcluded = createAction(
  '[Payment-Update-Form] payment to invoice dealer concluded');

export const selectedInvoiceUpdatedRequisitioned = createAction(
  '[Payment-Update-Form] selected invoice updated with payment',
  props<{selectedInvoiceId: number}>()
);

export const selectedInvoiceForUpdateAcquired = createAction(
  '[Dealer-Invoice-Workflow-Effects] selected invoice for update acquired',
  props<{acquiredInvoice: IInvoice}>()
);

export const selectedInvoiceUpdatedWithPaymentSuccessfully = createAction(
  '[Dealer-Invoice-Workflows-Effects : dealerInvoiceUpdateEffect] selected invoice updated with payment successfully',
);

export const selectedInvoiceUpdateWithPaymentErrored = createAction(
  '[Dealer-Invoice-Workflows-Effects : dealerInvoiceUpdateEffect] selected invoice update with payment error',
  props<{error: string}>()
);
