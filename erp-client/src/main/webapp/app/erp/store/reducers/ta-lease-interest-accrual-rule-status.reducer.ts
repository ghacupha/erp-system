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
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedEnRoute,
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromView,
  taLeaseInterestAccrualRuleCreationInitiatedEnRoute,
  taLeaseInterestAccrualRuleCreationInitiatedFromList,
  taLeaseInterestAccrualRuleCreationWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleDataHasMutated,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedEnRoute,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView,
  taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed,
  taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend,
  taLeaseInterestAccrualRuleUpdateInstanceAcquisitionFromBackendFailed,
} from '../actions/ta-lease-interest-accrual-rule-update-status.actions';
import { ITALeaseInterestAccrualRule } from '../../erp-accounts/ta-lease-interest-accrual-rule/ta-lease-interest-accrual-rule.model';

export const taLeaseInterestAccrualRuleUpdateFormStateSelector = 'taLeaseInterestAccrualRuleUpdateForm';

export interface TALeaseInterestAccrualRuleFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITALeaseInterestAccrualRule;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _taLeaseInterestAccrualRuleUpdateStateReducer = createReducer(
  initialState,

  on(taLeaseInterestAccrualRuleCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taLeaseInterestAccrualRuleCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromView, (state, { copiedInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView, (state, { editedInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredInstance }) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: backendAcquiredInstance,
      backEndFetchComplete: true,
    }
  })),

  on(taLeaseInterestAccrualRuleUpdateInstanceAcquisitionFromBackendFailed, (state) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      backEndFetchComplete: false,
    }
  })),

  on(taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleDataHasMutated, (state) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseInterestAccrualRuleCreationInitiatedFromList, (state) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taLeaseInterestAccrualRuleCreationInitiatedEnRoute, (state) => ({
    ...state,
    taLeaseInterestAccrualRuleFormState: {
      ...state.taLeaseInterestAccrualRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function taLeaseInterestAccrualRuleUpdateStateReducer(state: State = initialState, action: Action): State {
  return _taLeaseInterestAccrualRuleUpdateStateReducer(state, action);
}
