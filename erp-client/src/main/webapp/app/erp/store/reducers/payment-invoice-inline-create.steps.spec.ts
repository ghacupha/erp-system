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

import { defineFeature, loadFeature } from 'jest-cucumber';
import {
  initialPaymentInvoiceInlineCreateState,
  paymentInvoiceInlineCreateReducer,
  PaymentInvoiceInlineCreateState,
} from './payment-invoice-inline-create.reducer';
import {
  paymentInvoiceInlineCreateCancelled,
  paymentInvoiceInlineCreateCompleted,
  paymentInvoiceInlineCreateConsumed,
  paymentInvoiceInlineCreateStarted,
} from '../actions/payment-invoice-inline-create.actions';
import { IPaymentInvoice } from '../../erp-settlements/payment-invoice/payment-invoice.model';
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';

const feature = loadFeature('./payment-invoice-inline-create.feature');

defineFeature(feature, test => {
  let state: PaymentInvoiceInlineCreateState;
  const draftSettlement: ISettlement = { paymentNumber: 'DC317' };
  const savedInvoice: IPaymentInvoice = { id: 888, invoiceNumber: 'INV960' };

  test('Starting an Invoice inline-create captures the Settlement draft', ({ given, when, then, and }) => {
    given('a Settlement form has draft values entered', () => {
      state = initialPaymentInvoiceInlineCreateState;
    });

    when('it starts an inline Invoice create for the "paymentInvoices" field', () => {
      state = paymentInvoiceInlineCreateReducer(
        state,
        paymentInvoiceInlineCreateStarted({
          targetField: 'paymentInvoices',
          parentRoute: '/settlement/extension/new',
          parentFormSnapshot: draftSettlement,
        })
      );
    });

    then('the workflow is active', () => {
      expect(state.active).toBe(true);
    });

    and("the Settlement's draft is stored for later restoration", () => {
      expect(state.parentFormSnapshot).toEqual(draftSettlement);
      expect(state.targetField).toBe('paymentInvoices');
      expect(state.parentRoute).toBe('/settlement/extension/new');
    });

    and('no Invoice has been created yet', () => {
      expect(state.createdPaymentInvoice).toBeNull();
    });
  });

  test('Completing the Invoice inline-create records what was saved', ({ given, when, then, and }) => {
    given('a PaymentInvoice inline-create is active', () => {
      state = paymentInvoiceInlineCreateReducer(
        initialPaymentInvoiceInlineCreateState,
        paymentInvoiceInlineCreateStarted({
          targetField: 'paymentInvoices',
          parentRoute: '/settlement/extension/new',
          parentFormSnapshot: draftSettlement,
        })
      );
    });

    when('the Invoice form reports a saved Invoice', () => {
      state = paymentInvoiceInlineCreateReducer(state, paymentInvoiceInlineCreateCompleted({ createdPaymentInvoice: savedInvoice }));
    });

    then('the created Invoice is available to the Settlement form', () => {
      expect(state.createdPaymentInvoice).toEqual(savedInvoice);
    });

    and('the workflow is still active until explicitly consumed', () => {
      expect(state.active).toBe(true);
    });
  });

  test('Consuming the completion resets the workflow', ({ given, when, then, and }) => {
    given('a PaymentInvoice inline-create has a completed Invoice', () => {
      state = paymentInvoiceInlineCreateReducer(
        initialPaymentInvoiceInlineCreateState,
        paymentInvoiceInlineCreateStarted({
          targetField: 'paymentInvoices',
          parentRoute: '/settlement/extension/new',
          parentFormSnapshot: draftSettlement,
        })
      );
      state = paymentInvoiceInlineCreateReducer(state, paymentInvoiceInlineCreateCompleted({ createdPaymentInvoice: savedInvoice }));
    });

    when('the Settlement form consumes the completion', () => {
      state = paymentInvoiceInlineCreateReducer(state, paymentInvoiceInlineCreateConsumed());
    });

    then('the workflow is no longer active', () => {
      expect(state.active).toBe(false);
    });

    and('no draft or created Invoice remains', () => {
      expect(state.parentFormSnapshot).toBeNull();
      expect(state.createdPaymentInvoice).toBeNull();
    });
  });

  test('Cancelling an inline-create resets the workflow', ({ given, when, then }) => {
    given('a PaymentInvoice inline-create is active', () => {
      state = paymentInvoiceInlineCreateReducer(
        initialPaymentInvoiceInlineCreateState,
        paymentInvoiceInlineCreateStarted({
          targetField: 'paymentInvoices',
          parentRoute: '/settlement/extension/new',
          parentFormSnapshot: draftSettlement,
        })
      );
    });

    when('it is cancelled', () => {
      state = paymentInvoiceInlineCreateReducer(state, paymentInvoiceInlineCreateCancelled());
    });

    then('the workflow is no longer active', () => {
      expect(state.active).toBe(false);
    });
  });
});
