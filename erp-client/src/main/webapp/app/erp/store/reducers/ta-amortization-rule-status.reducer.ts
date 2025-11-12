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
  taAmortizationRuleCopyWorkflowInitiatedEnRoute,
  taAmortizationRuleCopyWorkflowInitiatedFromList,
  taAmortizationRuleCopyWorkflowInitiatedFromView,
  taAmortizationRuleCreationInitiatedEnRoute,
  taAmortizationRuleCreationInitiatedFromList,
  taAmortizationRuleCreationWorkflowInitiatedFromList,
  taAmortizationRuleDataHasMutated,
  taAmortizationRuleEditWorkflowInitiatedEnRoute,
  taAmortizationRuleEditWorkflowInitiatedFromList,
  taAmortizationRuleEditWorkflowInitiatedFromView,
  taAmortizationRuleUpdateFormHasBeenDestroyed
} from '../actions/ta-amortization-update-status.actions';
import { ITAAmortizationRule } from '../../erp-accounts/ta-amortization-rule/ta-amortization-rule.model';

export const taAmortizationRuleUpdateFormStateSelector = 'taAmortizationRuleUpdateForm';

export interface TAAmortizationRuleFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITAAmortizationRule;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _taAmortizationRuleUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(taAmortizationRuleCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(taAmortizationRuleCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(taAmortizationRuleEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleDataHasMutated, (state) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(taAmortizationRuleCreationInitiatedFromList, (state) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(taAmortizationRuleCreationInitiatedEnRoute, (state) => ({
    ...state,
    taAmortizationRuleFormState: {
      ...state.taAmortizationRuleFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function taAmortizationRuleUpdateStateReducer(state: State = initialState, action: Action): State {

  return _taAmortizationRuleUpdateStateReducer(state, action);
}
