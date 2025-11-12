///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { IPaymentInvoice } from 'app/erp/erp-settlements/payment-invoice/payment-invoice.model';

export const paymentInvoiceCreationInitiatedFromList = createAction(
  '[PaymentInvoice Creation: List] Payment-Invoice creation workflow initiated',
);

export const paymentInvoiceCopyWorkflowInitiatedEnRoute = createAction(
  '[PaymentInvoice Copy: Route] Payment-Invoice copy workflow initiated',
  props<{ copiedInstance: IPaymentInvoice }>()
);

export const paymentInvoiceCopyWorkflowInitiatedFromList = createAction(
  '[PaymentInvoice Copy: List] Payment-Invoice copy workflow initiated',
  props<{ copiedInstance: IPaymentInvoice }>()
);

export const paymentInvoiceCopyWorkflowInitiatedFromView = createAction(
  '[PaymentInvoice Copy: View] Payment-Invoice copy workflow initiated',
  props<{ copiedInstance: IPaymentInvoice }>()
);

export const paymentInvoiceEditWorkflowInitiatedEnRoute = createAction(
  '[PaymentInvoice Edit: Route] Payment-Invoice edit workflow initiated',
  props<{ editedInstance: IPaymentInvoice }>()
);

export const paymentInvoiceEditWorkflowInitiatedFromList = createAction(
  '[PaymentInvoice Edit: List] Payment-Invoice edit workflow initiated',
  props<{ editedInstance: IPaymentInvoice }>()
);

export const paymentInvoiceEditWorkflowInitiatedFromView = createAction(
  '[PaymentInvoice Edit: View] Payment-Invoice edit workflow initiated',
  props<{ editedInstance: IPaymentInvoice }>()
);

export const paymentInvoiceCreationInitiatedEnRoute = createAction(
  '[PaymentInvoice: Route] Payment-Invoice create workflow initiated',
);

export const paymentInvoiceCreationWorkflowInitiatedFromList = createAction(
  '[PaymentInvoice Create: List] Payment-Invoice create workflow initiated',
);

export const paymentInvoiceUpdateFormHasBeenDestroyed = createAction(
  '[PaymentInvoice Form] Payment-Invoice form destroyed',
);

export const paymentInvoiceDataHasMutated = createAction(
  '[PaymentInvoice Form] Payment-Invoice form data mutated',
);
