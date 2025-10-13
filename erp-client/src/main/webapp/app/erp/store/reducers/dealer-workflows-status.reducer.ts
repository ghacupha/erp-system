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

import {Action, createReducer, on} from '@ngrx/store';
import {initialState, State} from '../global-store.definition';
import {
  dealerAcquiredForPayment, dealerPaymentCategoryRequisitionFailed,
  payDealerButtonClicked, paymentCategoryAcquiredForPayment,
  paymentToDealerCompleted, paymentToDealerReset,
} from '../actions/dealer-workflows-status.actions';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IPayment } from '../../erp-pages/payments/payment/payment.model';
import { IPaymentCategory } from '../../erp-settlements/payments/payment-category/payment-category.model';

export const paymentToDealerWorkflowStateSelector = 'paymentToDealerWorkflows'

export interface DealerWorkflowState {
  selectedDealer: IDealer,
  dealerPayment: IPayment,
  paymentDealerCategory: IPaymentCategory | null,
  weArePayingADealer: boolean,
  errorMessage: string
}

const _dealerWorkflowStateReducer= createReducer(
  initialState,

  on(payDealerButtonClicked, (state, {selectedDealer, paymentDealerCategory}) => ({
     ...state,
    dealerWorkflowState: {
       ...state.dealerWorkflowState,
      selectedDealer,
      paymentDealerCategory,
      weArePayingADealer: true
    }
   })),

  on(paymentCategoryAcquiredForPayment, (state, {paymentDealerCategory}) => ({
     ...state,
    dealerWorkflowState: {
       ...state.dealerWorkflowState,
      paymentDealerCategory,
      weArePayingADealer: true
    }
   })),

  on(dealerPaymentCategoryRequisitionFailed, (state, {error}) => ({
     ...state,
    dealerWorkflowState: {
       ...state.dealerWorkflowState,
      paymentDealerCategory: {},
      errorMessage: error
    }
   })),

  on(dealerAcquiredForPayment, (state, {selectedDealer}) => ({
     ...state,
    dealerWorkflowState: {
       ...state.dealerWorkflowState,
      selectedDealer,
      weArePayingADealer: true
    }
   })),

  on(paymentToDealerReset, (state) => ({
    ...state,
    dealerWorkflowState: {
      ...state.dealerWorkflowState,
      selectedDealer: {},
      paymentDealerCategory: {},
      weArePayingADealer: false
    }
  })),

  on(paymentToDealerCompleted, (state) => ({
    ...state,
    dealerWorkflowState: {
      ...state.dealerWorkflowState,
      paymentDealerCategory: {},
      weArePayingADealer: false
    }
  })),
);

export function dealerWorkflowStateReducer(state: State = initialState, action: Action): State {

  return _dealerWorkflowStateReducer(state, action);
}

