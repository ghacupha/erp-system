import {IDealer} from '../../erp-pages/dealers/dealer/dealer.model';
import {Action, createReducer, on} from '@ngrx/store';
import {initialState, State} from '../global-store.definition';
import {
  payDealerButtonClicked,
  paymentToDealerCompleted,
  paymentToDealerInitiated
} from '../dealer-workflows-status.actions';
import {IPayment} from "../../erp-pages/payments/payment/payment.model";

export const paymentToDealerWorkflowStateSelector = 'paymentToDealerWorkflows'

export interface DealerWorkflowState {
  selectedDealer: IDealer,
  dealerPayment: IPayment,
  weArePayingADealer: boolean
}

const _dealerWorkflowStateReducer= createReducer(
  initialState,

  on(payDealerButtonClicked, (state, {selectedDealer}) => ({
     ...state,
    dealerWorkflowState: {
       ...state.dealerWorkflowState,
      selectedDealer,
      weArePayingADealer: true
    }
   })),

  on(paymentToDealerInitiated, (state, {dealerPayment}) => ({
     ...state,
    dealerWorkflowState: {
       ...state.dealerWorkflowState,
      dealerPayment,
      weArePayingADealer: true
    }
   })),

  on(paymentToDealerCompleted, (state) => ({
    ...state,
    dealerWorkflowState: {
      ...state.dealerWorkflowState,
      selectedDealer: {},
      weArePayingADealer: false
    }
  }))
);

export function dealerWorkflowStateReducer(state: State = initialState, action: Action): State {

  return _dealerWorkflowStateReducer(state, action);
}

