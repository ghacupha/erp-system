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

import {createAction, props} from "@ngrx/store";
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IInvoice } from '../../erp-pages/payments/invoice/invoice.model';
import { IPaymentLabel } from '../../erp-pages/payment-label/payment-label.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export const recordDealerInvoiceButtonClicked = createAction(
  '[Dealers Page] record dealer invoice button clicked',
  props<{
    selectedDealer: IDealer,
  }>()
);

export const dealerInvoicePaymentWorkflowCancelled = createAction(
  '[Invoice Update: Cancel Button] dealerInvoicePaymentWorkflowCancelled',
);

export const recordInvoicePaymentButtonClicked = createAction(
  '[Invoice Update Form] record invoice payment button clicked',
  props<{
    selectedInvoice: IInvoice,
  }>()
);

export const addPaymentToInvoiceButtonClicked = createAction(
  '[Invoice List] add payment to invoice button clicked',
  props<{
    selectedInvoice: IInvoice,
    selectedDealer: IDealer,
  }>()
);

export const dealerAcquiredForInvoicedPayment = createAction(
  '[Dealer-Invoice-payment-resolve-service] dealer acquired for invoiced payment',
  props<{
    paymentLabels: IPaymentLabel[],
    placeholders: IPlaceholder[],
  }>()
);

export const invoiceAcquiredForPaymentWithLabels = createAction(
  '[Invoice-List] invoice acquired for payment with labels',
  props<{paymentLabels: IPaymentLabel[]}>()
);

export const invoiceAcquiredForPaymentWithPlaceholders = createAction(
  '[Invoice-List] invoice acquired for payment with placeholders',
  props<{placeholders: IPlaceholder[]}>()
);

export const dealerInvoiceStateReset = createAction(
  '[System] dealer invoice state has been reset');

export const paymentToInvoiceDealerConcluded = createAction(
  '[Payment-Update-Form] payment to invoice dealer concluded');

export const selectedInvoiceUpdatedRequisitioned = createAction(
  '[Payment-Update-Form] selected invoice updated with payment',
  props<{selectedInvoiceId: number}>()
);

export const selectedInvoiceForUpdateAcquired = createAction(
  '[Dealer-Invoice-Workflow-Effects] selected invoice for update acquired',
  props<{acquiredInvoice: IInvoice}>()
);

export const selectedInvoiceUpdatedWithPaymentSuccessfully = createAction(
  '[Dealer-Invoice-Workflows-Effects : dealerInvoiceUpdateEffect] selected invoice updated with payment successfully',
);

export const selectedInvoiceUpdateWithPaymentErrored = createAction(
  '[Dealer-Invoice-Workflows-Effects : dealerInvoiceUpdateEffect] selected invoice update with payment error',
  props<{error: string}>()
);
