// @typescript-eslint/no-unnecessary-condition
import {createFeatureSelector, createSelector} from "@ngrx/store";
import {State} from "./global-store.definition";
import {paymentToDealerWorkflowStateSelector} from "./reducers/dealer-workflows-status.reducer";

export const paymentToDealerWorkflows = createFeatureSelector<State>(paymentToDealerWorkflowStateSelector);

export const dealerPaymentSelectedDealer = createSelector(
  paymentToDealerWorkflows,
  state => state.dealerWorkflowState.selectedDealer
);

export const dealerPaymentStatus = createSelector(
  paymentToDealerWorkflows,
  state => state.dealerWorkflowState.weArePayingADealer
);
