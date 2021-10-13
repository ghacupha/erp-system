import {createFeatureSelector, createSelector} from "@ngrx/store";
import {State} from "../global-store.definition";
import {dealerInvoiceWorkflowStateSelector} from "../reducers/dealer-invoice-workflows-status.reducer";

export const dealerInvoiceWorkflows = createFeatureSelector<State>(dealerInvoiceWorkflowStateSelector);

export const dealerInvoiceSelectedDealer = createSelector(
  dealerInvoiceWorkflows,
  state => state.dealerInvoiceWorkflowState.selectedDealer
);

export const dealerInvoiceSelected = createSelector(
  dealerInvoiceWorkflows,
  state => state.dealerInvoiceWorkflowState.selectedInvoice
);

export const dealerInvoiceSelectedPayment = createSelector(
  dealerInvoiceWorkflows,
  state => state.dealerInvoiceWorkflowState.selectedPayment
);

export const dealerInvoicePaymentLabels = createSelector(
  dealerInvoiceWorkflows,
  state => state.dealerInvoiceWorkflowState.selectedInvoiceLabels
);

export const dealerInvoicePlaceholders = createSelector(
  dealerInvoiceWorkflows,
  state => state.dealerInvoiceWorkflowState.selectedInvoicePlaceholders
);

export const dealerInvoicePaymentState = createSelector(
  dealerInvoiceWorkflows,
  state => state.dealerInvoiceWorkflowState.weArePayingAnInvoiceDealer
);
