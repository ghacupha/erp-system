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

// @typescript-eslint/no-unnecessary-condition
import {createFeatureSelector, createSelector} from "@ngrx/store";
import {State} from "../global-store.definition";
import {paymentToDealerWorkflowStateSelector} from "../reducers/dealer-workflows-status.reducer";

export const paymentToDealerWorkflows = createFeatureSelector<State>(paymentToDealerWorkflowStateSelector);

export const dealerPaymentSelectedDealer = createSelector(
  paymentToDealerWorkflows,
  state => state.dealerWorkflowState.selectedDealer
);

export const dealerPaymentStatus = createSelector(
  paymentToDealerWorkflows,
  state => state.dealerWorkflowState.weArePayingADealer
);

export const dealerCategory = createSelector(
  paymentToDealerWorkflows,
  state => state.dealerWorkflowState.paymentDealerCategory
);
