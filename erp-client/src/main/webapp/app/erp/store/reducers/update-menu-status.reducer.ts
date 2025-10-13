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

import {Action, createReducer, on} from "@ngrx/store";
import {initialState, State} from "../global-store.definition";
import {
  newPaymentButtonClicked,
  paymentCopyButtonClicked,
  paymentCopyInitiated,
  paymentEditInitiated,
  paymentSaveButtonClicked,
  paymentUpdateButtonClicked,
  paymentUpdateCancelButtonClicked,
  paymentUpdateConcluded,
  paymentUpdateErrorHasOccurred
} from "../actions/update-menu-status.actions";
import { IPayment } from '../../erp-pages/payments/payment/payment.model';

export const paymentUpdateFormStateSelector = 'paymentUpdateForm';

export interface PaymentsFormState {
  selectedPayment: IPayment,
  weAreCopying: boolean
  weAreEditing: boolean
  weAreCreating: boolean
}

const _paymentUpdateStateReducer = createReducer(
  initialState,

  on(paymentCopyInitiated, (state, {copiedPayment}) => ({
    ...state,
    paymentsFormState: {
      ...state.paymentsFormState,
      selectedPayment: copiedPayment,
      weAreCopying: true
    }
  })),

  on(paymentCopyButtonClicked, (state) => ({
    ...state,
    paymentsFormState: {
      ...state.paymentsFormState,
      weAreCopying: false
    }
  })),

  on(paymentEditInitiated, (state, {editPayment}) => ({
    ...state,
    paymentsFormState: {
      ...state.paymentsFormState,
      selectedPayment: editPayment,
      weAreEditing: true
    }
  })),

  on(paymentUpdateButtonClicked, state => ({
    ...state,
    paymentsFormState: {
      ...state.paymentsFormState,
      selectedPayment: {},
      weAreEditing: false
    }
  })),

  on(paymentUpdateConcluded, state => ({
    ...state,
    paymentsFormState: {
      ...state.paymentsFormState,
      selectedPayment: {},
      weAreEditing: false,
      weAreCopying: false,
      weAreCreating: false
    }
  })),

  on(newPaymentButtonClicked, (state, {newPayment}) => ({
      ...state,
      paymentsFormState: {
        ...state.paymentsFormState,
        selectedPayment: newPayment,
        weAreCreating: true
      }
    }
  )),

  on(paymentSaveButtonClicked, state => ({
      ...state,
      paymentsFormState: {
        ...state.paymentsFormState,
        selectedPayment: {},
        weAreCreating: false
      }
    }
  )),

  on(paymentUpdateCancelButtonClicked, state => ({
      ...state,
      paymentsFormState: {
        ...state.paymentsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
      }
    }
  )),

  on(paymentUpdateErrorHasOccurred, state => ({
      ...state,
      paymentsFormState: {
        ...state.paymentsFormState,
        selectedPayment: {},
        weAreCreating: false,
        weAreEditing: false,
        weAreCopying: false,
      }
    }
  )),
);

export function paymentUpdateStateReducer(state: State = initialState, action: Action): State {

  return _paymentUpdateStateReducer(state, action);
}
