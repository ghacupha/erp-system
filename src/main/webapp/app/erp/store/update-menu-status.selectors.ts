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

import {createFeatureSelector, createSelector} from "@ngrx/store";
import {paymentUpdateFormStateSelector} from "./update-menu-status.reducer";
import {State} from "./global-store.definition";

export const paymentStatusFeatureSelector = createFeatureSelector<State>(paymentUpdateFormStateSelector);

export const updateSelectedPayment = createSelector(
  paymentStatusFeatureSelector,
  state => state.paymentsFormState.selectedPayment
);

export const editingPaymentStatus = createSelector(
  paymentStatusFeatureSelector,
  state => state.paymentsFormState.weAreEditing
);

export const creatingPaymentStatus = createSelector(
  paymentStatusFeatureSelector,
  state => state.paymentsFormState.weAreCreating
);

export const copyingPaymentStatus = createSelector(
  paymentStatusFeatureSelector,
  state => state.paymentsFormState.weAreCopying
);
