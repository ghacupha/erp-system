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

import {createAction, props} from '@ngrx/store';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IPaymentCategory } from '../../erp-settlements/payments/payment-category/payment-category.model';

export const payDealerButtonClicked = createAction(
  '[Dealer-category-service] pay dealers button clicked',
  props<{
    selectedDealer: IDealer,
    paymentDealerCategory: IPaymentCategory
  }>()
);

export const dealerAcquiredForPayment = createAction(
  '[Dealers page] dealer acquired for payment',
  props<{selectedDealer: IDealer}>()
);

export const requisitionForDealerCategoryInitiated = createAction(
  '[Dealers page] requisition for dealer-category initiated',
  props<{selectedDealer: IDealer}>()
);

export const paymentCategoryAcquiredForPayment = createAction(
  '[Dealer-payments-effects] payment category acquired for payment',
  props<{paymentDealerCategory: IPaymentCategory}>()
);

export const dealerPaymentCategoryRequisitionFailed = createAction(
  '[Dealer-payments-effects] dealer payment category',
  props<{error: string}>()
);

export const paymentDealerCategoryAcquired = createAction(
  '[Dealer-Category-Service] payment dealer category acquired',
  props<{paymentDealerCategory: IPaymentCategory}>()
);

export const paymentToDealerReset = createAction(
  '[Payment-Update-Form] payment details with dealer reset',
);

export const paymentToDealerCompleted = createAction(
  '[Payment-Update-Form pay button] payment details with dealer completed',
);
