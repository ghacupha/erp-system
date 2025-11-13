///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import {Action, createReducer, on} from "@ngrx/store";
import {initialState, State} from "../global-store.definition";
import {
  addPaymentToInvoiceButtonClicked,
  dealerAcquiredForInvoicedPayment, dealerInvoicePaymentWorkflowCancelled,
  dealerInvoiceStateReset,
  invoiceAcquiredForPaymentWithLabels,
  invoiceAcquiredForPaymentWithPlaceholders,
  paymentToInvoiceDealerConcluded,
  recordDealerInvoiceButtonClicked,
  recordInvoicePaymentButtonClicked,
  selectedInvoiceForUpdateAcquired,
  selectedInvoiceUpdatedRequisitioned,
  selectedInvoiceUpdatedWithPaymentSuccessfully,
  selectedInvoiceUpdateWithPaymentErrored
} from "../actions/dealer-invoice-workflows-status.actions";
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IInvoice } from '../../erp-pages/payments/invoice/invoice.model';
import { IPayment } from '../../erp-pages/payments/payment/payment.model';
import { IPaymentLabel } from '../../erp-pages/payment-label/payment-label.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export const dealerInvoiceWorkflowStateSelector = 'recordDealerInvoiceWorkflows'

export interface DealerInvoiceWorkflowState {
  invoiceDealer: IDealer,
  selectedInvoice: IInvoice,
  invoicePayment: IPayment,
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
        invoiceDealer: selectedDealer,
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

    on(dealerInvoicePaymentWorkflowCancelled, (state) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedInvoice: {},
        invoiceDealer: {},
        weArePayingAnInvoiceDealer: false,
        invoicePayment: {},
        selectedInvoiceLabels: [],
        selectedInvoicePlaceholders: [],
        errorMessage: '',
      }
    })),

    on(addPaymentToInvoiceButtonClicked, (state, {selectedInvoice, selectedDealer}) => ({
      ...state,
      dealerInvoiceWorkflowState: {
        ...state.dealerInvoiceWorkflowState,
        selectedInvoice,
        invoiceDealer: selectedDealer,
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
        invoiceDealer: {},
        selectedInvoice: {},
        weArePayingAnInvoiceDealer: false,
        invoicePayment: {},
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
        invoiceDealer: {},
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
