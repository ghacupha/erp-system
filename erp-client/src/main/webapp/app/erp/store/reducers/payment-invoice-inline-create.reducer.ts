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

import { Action, createReducer, on } from '@ngrx/store';
import { IPaymentInvoice } from '../../erp-settlements/payment-invoice/payment-invoice.model';
import {
  paymentInvoiceInlineCreateCancelled,
  paymentInvoiceInlineCreateCompleted,
  paymentInvoiceInlineCreateConsumed,
  paymentInvoiceInlineCreateStarted,
} from '../actions/payment-invoice-inline-create.actions';

export const paymentInvoiceInlineCreateFeatureKey = 'paymentInvoiceInlineCreateState';

export interface PaymentInvoiceInlineCreateState {
  active: boolean;
  targetField: string;
  parentRoute: string;
  parentFormSnapshot: unknown;
  createdPaymentInvoice: IPaymentInvoice | null;
}

export const initialPaymentInvoiceInlineCreateState: PaymentInvoiceInlineCreateState = {
  active: false,
  targetField: '',
  parentRoute: '',
  parentFormSnapshot: null,
  createdPaymentInvoice: null,
};

const _paymentInvoiceInlineCreateReducer = createReducer(
  initialPaymentInvoiceInlineCreateState,

  on(paymentInvoiceInlineCreateStarted, (state, { targetField, parentRoute, parentFormSnapshot }) => ({
    ...state,
    active: true,
    targetField,
    parentRoute,
    parentFormSnapshot,
    createdPaymentInvoice: null,
  })),

  on(paymentInvoiceInlineCreateCompleted, (state, { createdPaymentInvoice }) => ({
    ...state,
    createdPaymentInvoice,
  })),

  on(paymentInvoiceInlineCreateConsumed, () => initialPaymentInvoiceInlineCreateState),

  on(paymentInvoiceInlineCreateCancelled, () => initialPaymentInvoiceInlineCreateState)
);

export function paymentInvoiceInlineCreateReducer(
  state: PaymentInvoiceInlineCreateState = initialPaymentInvoiceInlineCreateState,
  action: Action
): PaymentInvoiceInlineCreateState {
  return _paymentInvoiceInlineCreateReducer(state, action);
}
