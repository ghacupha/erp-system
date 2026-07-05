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

import { IRouInitialDirectCost } from '../../erp-leases/rou-initial-direct-cost/rou-initial-direct-cost.model';
import { initialState, State } from '../global-store.definition';
import { Action, createReducer, on } from '@ngrx/store';
import {
  rouInitialDirectCostCopyWorkflowInitiatedEnRoute,
  rouInitialDirectCostCopyWorkflowInitiatedFromList,
  rouInitialDirectCostCopyWorkflowInitiatedFromView,
  rouInitialDirectCostCreationInitiatedEnRoute,
  rouInitialDirectCostCreationInitiatedFromList,
  rouInitialDirectCostCreationWorkflowInitiatedFromList,
  rouInitialDirectCostDataHasMutated,
  rouInitialDirectCostEditWorkflowInitiatedEnRoute,
  rouInitialDirectCostEditWorkflowInitiatedFromList,
  rouInitialDirectCostEditWorkflowInitiatedFromView,
  rouInitialDirectCostUpdateFormHasBeenDestroyed
} from '../actions/rou-initial-direct-cost-update-status.actions';

export const rouInitialDirectCostUpdateFormStateSelector = 'rouInitialDirectCostUpdateForm';

export interface RouInitialDirectCostFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IRouInitialDirectCost;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _rouInitialDirectCostUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(rouInitialDirectCostCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  // workflows for copy
  on(rouInitialDirectCostCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  // workflows for edit
  on(rouInitialDirectCostEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostDataHasMutated, (state) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouInitialDirectCostCreationInitiatedFromList, (state) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(rouInitialDirectCostCreationInitiatedEnRoute, (state) => ({
    ...state,
    rouInitialDirectCostFormState: {
      ...state.rouInitialDirectCostFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function rouInitialDirectCostUpdateStateReducer(state: State = initialState, action: Action): State {
  return _rouInitialDirectCostUpdateStateReducer(state, action);
}
