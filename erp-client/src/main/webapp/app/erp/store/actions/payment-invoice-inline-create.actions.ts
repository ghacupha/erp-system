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

import { createAction, props } from '@ngrx/store';
import { IPaymentInvoice } from '../../erp-settlements/payment-invoice/payment-invoice.model';

/**
 * Dedicated to PaymentInvoice specifically - see settlement-inline-create.actions.ts for the
 * rationale (one dedicated slice per child entity type rather than a generic shared one). The
 * parent form (SettlementUpdateComponent, whether or not IT is itself mid inline-create from a
 * Prepayment Account) dispatches paymentInvoiceInlineCreateStarted with its own full form
 * snapshot right before navigating to the Invoice create route.
 */

export const paymentInvoiceInlineCreateStarted = createAction(
  '[PaymentInvoice Inline Create] started',
  props<{
    targetField: string;
    parentRoute: string;
    parentFormSnapshot: unknown;
  }>()
);

export const paymentInvoiceInlineCreateCompleted = createAction(
  '[PaymentInvoice Inline Create] completed',
  props<{ createdPaymentInvoice: IPaymentInvoice }>()
);

export const paymentInvoiceInlineCreateConsumed = createAction('[PaymentInvoice Inline Create] consumed');

export const paymentInvoiceInlineCreateCancelled = createAction('[PaymentInvoice Inline Create] cancelled');
