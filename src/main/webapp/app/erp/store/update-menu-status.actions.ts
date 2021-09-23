///
/// Payment Records - Payment records is part of the ERP System
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import {IPayment} from "../erp-pages/payments/payment/payment.model";

export const paymentCopyInitiated = createAction(
  '[Payments Page] payment copy initiated',
  props<{copiedPayment: IPayment}>()
);

export const paymentCopyButtonClicked = createAction(
  '[Payment Update Form: Copy Button] payment copy button clicked'
);

export const paymentEditInitiated = createAction(
  '[Payments Page] payment edit initiated',
  props<{editPayment: IPayment}>()
);

export const paymentUpdateButtonClicked = createAction(
  '[Payment Edit Form: Update Button] payment update button clicked'
);

export const newPaymentButtonClicked = createAction(
  '[Payments New Button] new payment button clicked',
  props<{newPayment: IPayment}>()
);

export const paymentSaveButtonClicked = createAction(
  '[Payment Update Form: Save Button] payment save button clicked'
);

export const paymentUpdateCancelButtonClicked = createAction(
  '[Payment Update Form: Cancel Button] payment update cancel button clicked'
);

export const paymentUpdateErrorHasOccurred = createAction(
  '[Payment Update Form: Error] Payment update error encountered'
);
