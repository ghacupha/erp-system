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

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import {
  taRecognitionRouRuleCopyWorkflowInitiatedEnRoute,
  taRecognitionRouRuleCopyWorkflowInitiatedFromList,
  taRecognitionRouRuleCopyWorkflowInitiatedFromView,
  taRecognitionRouRuleCreationInitiatedEnRoute,
  taRecognitionRouRuleCreationInitiatedFromList,
  taRecognitionRouRuleCreationWorkflowInitiatedFromList,
  taRecognitionRouRuleDataHasMutated,
  taRecognitionRouRuleEditWorkflowInitiatedEnRoute,
  taRecognitionRouRuleEditWorkflowInitiatedFromList,
  taRecognitionRouRuleEditWorkflowInitiatedFromView,
  taRecognitionRouRuleUpdateFormHasBeenDestroyed,
  taRecognitionRouRuleUpdateInstanceAcquiredFromBackend,
  taRecognitionRouRuleUpdateInstanceAcquisitionFromBackendFailed,
} from '../actions/ta-recognition-rou-rule-update-status.actions';
import { ITARecognitionROURule } from '../../erp-accounts/ta-recognition-rou-rule/ta-recognition-rou-rule.model';

export const taRecognitionRouRuleUpdateFormStateSelector = 'taRecognitionRouRuleUpdateForm';

export interface TARecognitionROURuleFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITARecognitionROURule;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _taRecognitionRouRuleUpdateStateReducer = createReducer(
  initialState,

  on(taRecognitionRouRuleCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taRecognitionRouRuleCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleCopyWorkflowInitiatedFromView, (state, { copiedInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleEditWorkflowInitiatedFromView, (state, { editedInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredInstance }) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: backendAcquiredInstance,
      backEndFetchComplete: true,
    }
  })),

  on(taRecognitionRouRuleUpdateInstanceAcquisitionFromBackendFailed, (state) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      backEndFetchComplete: false,
    }
  })),

  on(taRecognitionRouRuleUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleDataHasMutated, (state) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taRecognitionRouRuleCreationInitiatedFromList, (state) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taRecognitionRouRuleCreationInitiatedEnRoute, (state) => ({
    ...state,
    taRecognitionRouRuleFormState: {
      ...state.taRecognitionRouRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function taRecognitionRouRuleUpdateStateReducer(state: State = initialState, action: Action): State {
  return _taRecognitionRouRuleUpdateStateReducer(state, action);
}
