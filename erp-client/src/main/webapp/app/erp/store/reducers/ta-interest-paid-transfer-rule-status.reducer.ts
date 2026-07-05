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
  taInterestPaidTransferRuleCopyWorkflowInitiatedEnRoute,
  taInterestPaidTransferRuleCopyWorkflowInitiatedFromList,
  taInterestPaidTransferRuleCopyWorkflowInitiatedFromView,
  taInterestPaidTransferRuleCreationInitiatedEnRoute,
  taInterestPaidTransferRuleCreationInitiatedFromList,
  taInterestPaidTransferRuleCreationWorkflowInitiatedFromList,
  taInterestPaidTransferRuleDataHasMutated,
  taInterestPaidTransferRuleEditWorkflowInitiatedEnRoute,
  taInterestPaidTransferRuleEditWorkflowInitiatedFromList,
  taInterestPaidTransferRuleEditWorkflowInitiatedFromView,
  taInterestPaidTransferRuleUpdateFormHasBeenDestroyed,
  taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend,
  taInterestPaidTransferRuleUpdateInstanceAcquisitionFromBackendFailed,
} from '../actions/ta-interest-paid-transfer-rule-update-status.actions';
import { ITAInterestPaidTransferRule } from '../../erp-accounts/ta-interest-paid-transfer-rule/ta-interest-paid-transfer-rule.model';

export const taInterestPaidTransferRuleUpdateFormStateSelector = 'taInterestPaidTransferRuleUpdateForm';

export interface TAInterestPaidTransferRuleFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITAInterestPaidTransferRule;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _taInterestPaidTransferRuleUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(taInterestPaidTransferRuleCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  // workflows for copy
  on(taInterestPaidTransferRuleCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleCopyWorkflowInitiatedFromView, (state, { copiedInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  // workflows for edit
  on(taInterestPaidTransferRuleEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleEditWorkflowInitiatedFromView, (state, { editedInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredInstance }) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: backendAcquiredInstance,
      backEndFetchComplete: true,
    }
  })),

  on(taInterestPaidTransferRuleUpdateInstanceAcquisitionFromBackendFailed, (state) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      backEndFetchComplete: false,
    }
  })),

  on(taInterestPaidTransferRuleUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleDataHasMutated, (state) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taInterestPaidTransferRuleCreationInitiatedFromList, (state) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taInterestPaidTransferRuleCreationInitiatedEnRoute, (state) => ({
    ...state,
    taInterestPaidTransferRuleFormState: {
      ...state.taInterestPaidTransferRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function taInterestPaidTransferRuleUpdateStateReducer(state: State = initialState, action: Action): State {
  return _taInterestPaidTransferRuleUpdateStateReducer(state, action);
}
