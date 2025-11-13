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
  taLeaseRecognitionRuleCopyWorkflowInitiatedEnRoute,
  taLeaseRecognitionRuleCopyWorkflowInitiatedFromList,
  taLeaseRecognitionRuleCopyWorkflowInitiatedFromView,
  taLeaseRecognitionRuleCreationInitiatedEnRoute,
  taLeaseRecognitionRuleCreationInitiatedFromList,
  taLeaseRecognitionRuleCreationWorkflowInitiatedFromList,
  taLeaseRecognitionRuleDataHasMutated,
  taLeaseRecognitionRuleEditWorkflowInitiatedEnRoute,
  taLeaseRecognitionRuleEditWorkflowInitiatedFromList,
  taLeaseRecognitionRuleEditWorkflowInitiatedFromView,
  taLeaseRecognitionRuleUpdateFormHasBeenDestroyed,
  taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend,
  taLeaseRecognitionRuleUpdateInstanceAcquisitionFromBackendFailed,
} from '../actions/ta-lease-recognition-rule-update-status.actions';
import { ITALeaseRecognitionRule } from '../../erp-accounts/ta-lease-recognition-rule/ta-lease-recognition-rule.model';

export const taLeaseRecognitionRuleUpdateFormStateSelector = 'taLeaseRecognitionRuleUpdateForm';

export interface TALeaseRecognitionRuleFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITALeaseRecognitionRule;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _taLeaseRecognitionRuleUpdateStateReducer = createReducer(
  initialState,

  on(taLeaseRecognitionRuleCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taLeaseRecognitionRuleCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleCopyWorkflowInitiatedFromView, (state, { copiedInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleEditWorkflowInitiatedFromView, (state, { editedInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredInstance }) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: backendAcquiredInstance,
      backEndFetchComplete: true,
    }
  })),

  on(taLeaseRecognitionRuleUpdateInstanceAcquisitionFromBackendFailed, (state) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      backEndFetchComplete: false,
    }
  })),

  on(taLeaseRecognitionRuleUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleDataHasMutated, (state) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taLeaseRecognitionRuleCreationInitiatedFromList, (state) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taLeaseRecognitionRuleCreationInitiatedEnRoute, (state) => ({
    ...state,
    taLeaseRecognitionRuleFormState: {
      ...state.taLeaseRecognitionRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function taLeaseRecognitionRuleUpdateStateReducer(state: State = initialState, action: Action): State {
  return _taLeaseRecognitionRuleUpdateStateReducer(state, action);
}
