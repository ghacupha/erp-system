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

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import {
  taLeaseRepaymentRuleCopyWorkflowInitiatedEnRoute,
  taLeaseRepaymentRuleCopyWorkflowInitiatedFromList,
  taLeaseRepaymentRuleCopyWorkflowInitiatedFromView,
  taLeaseRepaymentRuleCreationInitiatedEnRoute,
  taLeaseRepaymentRuleCreationInitiatedFromList,
  taLeaseRepaymentRuleCreationWorkflowInitiatedFromList,
  taLeaseRepaymentRuleDataHasMutated,
  taLeaseRepaymentRuleEditWorkflowInitiatedEnRoute,
  taLeaseRepaymentRuleEditWorkflowInitiatedFromList,
  taLeaseRepaymentRuleEditWorkflowInitiatedFromView,
  taLeaseRepaymentRuleUpdateFormHasBeenDestroyed,
  taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend,
  taLeaseRepaymentRuleUpdateInstanceAcquisitionFromBackendFailed,
} from '../actions/ta-lease-repayment-rule-update-status.actions';
import { ITALeaseRepaymentRule } from '../../erp-accounts/ta-lease-repayment-rule/ta-lease-repayment-rule.model';

export const taLeaseRepaymentRuleUpdateFormStateSelector = 'taLeaseRepaymentRuleUpdateForm';

export interface TALeaseRepaymentRuleFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITALeaseRepaymentRule;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _taLeaseRepaymentRuleUpdateStateReducer = createReducer(
  initialState,

  on(taLeaseRepaymentRuleCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taLeaseRepaymentRuleCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleCopyWorkflowInitiatedFromView, (state, { copiedInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleEditWorkflowInitiatedFromView, (state, { editedInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredInstance }) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: backendAcquiredInstance,
      backEndFetchComplete: true,
    }
  })),

  on(taLeaseRepaymentRuleUpdateInstanceAcquisitionFromBackendFailed, (state) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      backEndFetchComplete: false,
    }
  })),

  on(taLeaseRepaymentRuleUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleDataHasMutated, (state) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRepaymentRuleCreationInitiatedFromList, (state) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taLeaseRepaymentRuleCreationInitiatedEnRoute, (state) => ({
    ...state,
    taLeaseRepaymentRuleFormState: {
      ...state.taLeaseRepaymentRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function taLeaseRepaymentRuleUpdateStateReducer(state: State = initialState, action: Action): State {
  return _taLeaseRepaymentRuleUpdateStateReducer(state, action);
}
