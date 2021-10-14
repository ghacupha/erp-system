import {IDealer} from "../../erp-pages/dealers/dealer/dealer.model";
import {IInvoice} from "../../erp-pages/payments/invoice/invoice.model";
import {Action, createReducer, on} from "@ngrx/store";
import {initialState, State} from "../global-store.definition";
import {
  addPaymentToInvoiceButtonClicked,
  dealerAcquiredForInvoicedPayment,
  dealerInvoiceStateReset,
  invoiceAcquiredForPaymentWithLabels,
  invoiceAcquiredForPaymentWithPlaceholders,
  paymentToInvoiceDealerConcluded,
  recordDealerInvoiceButtonClicked,
  recordInvoicePaymentButtonClicked, selectedInvoiceForUpdateAcquired, selectedInvoiceUpdatedRequisitioned,
  selectedInvoiceUpdatedWithPaymentSuccessfully,
  selectedInvoiceUpdateWithPaymentErrored
} from "../actions/dealer-invoice-workflows-status.actions";
import {IPayment} from "../../erp-pages/payments/payment/payment.model";
import {IPaymentLabel} from "../../erp-pages/payment-label/payment-label.model";
import {IPlaceholder} from "../../../entities/erpService/placeholder/placeholder.model";

export const dealerInvoiceWorkflowStateSelector = 'recordDealerInvoiceWorkflows'

export interface DealerInvoiceWorkflowState {
  selectedDealer: IDealer,
  selectedInvoice: IInvoice,
  selectedPayment: IPayment,
  selectedInvoiceLabels: IPaymentLabel[],
  selectedInvoicePlaceholders: IPlaceholder[],
  weArePayingAnInvoiceDealer: boolean,
  errorMessage: string
}

const _dealerInvoiceWorkflowStateReducer= createReducer(
    initialState,

    on(recordDealerInvoiceButtonClicked, (state, {selectedDealer}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedDealer,
        weArePayingAnInvoiceDealer: true,
      }
    })),

    on(recordInvoicePaymentButtonClicked, (state, {selectedInvoice}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedInvoice,
        weArePayingAnInvoiceDealer: true
      }
    })),

    on(addPaymentToInvoiceButtonClicked, (state, {selectedInvoice, selectedDealer}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedInvoice,
        selectedDealer,
        weArePayingAnInvoiceDealer: true
      }
    })),

    on(dealerAcquiredForInvoicedPayment, (state, {paymentLabels, placeholders,}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        paymentLabels,
        placeholders,
        weArePayingAnInvoiceDealer: true
      }
    })),

    on(invoiceAcquiredForPaymentWithLabels, (state, {paymentLabels}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        paymentLabels,
        weArePayingAnInvoiceDealer: true
      }
    })),

    on(invoiceAcquiredForPaymentWithPlaceholders, (state, {placeholders}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        placeholders,
        weArePayingAnInvoiceDealer: true
      }
    })),

    on(paymentToInvoiceDealerConcluded, state => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        // TODO REMOVE this after invoice update selectedDealer: {},
        // TODO REMOVE this after invoice update selectedInvoice: {},
        // TODO REMOVE this after invoice update weArePayingAnInvoiceDealer: false,
        // TODO REMOVE this after invoice update selectedPayment: {},
        selectedInvoiceLabels: [],
        selectedInvoicePlaceholders: [],
        errorMessage: '',
      }
    })),

    on(selectedInvoiceUpdatedWithPaymentSuccessfully, state => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedPayment: {},
        selectedInvoice: {},
      }
    })),

    on(selectedInvoiceUpdatedRequisitioned, state => ({
      ...state,
    })),

    on(selectedInvoiceForUpdateAcquired, (state, {acquiredInvoice}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedInvoice: acquiredInvoice,
      }
    })),

    on(selectedInvoiceUpdateWithPaymentErrored, (state, {error}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        errorMessage: error
      }
    })),

    on(dealerInvoiceStateReset, state => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedDealer: {},
        selectedInvoice: {},
        weArePayingAnInvoiceDealer: false,
        selectedPayment: {},
        selectedInvoiceLabels: [],
        selectedInvoicePlaceholders: [],
        errorMessage: '',
      }
    })),

  );

export function dealerInvoiceWorkflowStateReducer(state: State = initialState, action: Action): State {

  return _dealerInvoiceWorkflowStateReducer(state, action);
}
