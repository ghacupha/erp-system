///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import { IPaymentInvoice } from '../../erp-settlements/payment-invoice/payment-invoice.model';
import {
  paymentInvoiceCopyWorkflowInitiatedEnRoute,
  paymentInvoiceCopyWorkflowInitiatedFromList,
  paymentInvoiceCopyWorkflowInitiatedFromView,
  paymentInvoiceCreationInitiatedEnRoute,
  paymentInvoiceCreationInitiatedFromList,
  paymentInvoiceCreationWorkflowInitiatedFromList,
  paymentInvoiceDataHasMutated,
  paymentInvoiceEditWorkflowInitiatedEnRoute,
  paymentInvoiceEditWorkflowInitiatedFromList,
  paymentInvoiceEditWorkflowInitiatedFromView,
  paymentInvoiceUpdateFormHasBeenDestroyed
} from '../actions/payment-invoice-workflow-status.action';

export const paymentInvoiceUpdateFormStateSelector = 'paymentInvoiceUpdateForm';

export interface PaymentInvoiceFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IPaymentInvoice;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _paymentInvoiceUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(paymentInvoiceCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(paymentInvoiceCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(paymentInvoiceEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceDataHasMutated, (state) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(paymentInvoiceCreationInitiatedFromList, (state) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(paymentInvoiceCreationInitiatedEnRoute, (state) => ({
    ...state,
    paymentInvoiceFormState: {
      ...state.paymentInvoiceFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function paymentInvoiceUpdateStateReducer(state: State = initialState, action: Action): State {

  return _paymentInvoiceUpdateStateReducer(state, action);
}
