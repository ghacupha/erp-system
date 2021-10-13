import {IDealer} from "../../erp-pages/dealers/dealer/dealer.model";
import {IInvoice} from "../../erp-pages/payments/invoice/invoice.model";
import {Action, createReducer, on} from "@ngrx/store";
import {initialState, State} from "../global-store.definition";
import {
  addPaymentToInvoiceButtonClicked, dealerAcquiredForInvoicedPayment,
  dealerInvoiceStateReset, invoiceAcquiredForPaymentWithLabels, invoiceAcquiredForPaymentWithPlaceholders,
  recordDealerInvoiceButtonClicked, recordInvoicePaymentButtonClicked
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
