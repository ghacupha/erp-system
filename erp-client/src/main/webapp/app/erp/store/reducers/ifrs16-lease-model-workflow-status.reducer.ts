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

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import {
  ifrs16LeaseContractCopyWorkflowInitiatedEnRoute,
  ifrs16LeaseContractCopyWorkflowInitiatedFromList,
  ifrs16LeaseContractCopyWorkflowInitiatedFromView,
  ifrs16LeaseContractCreationInitiatedEnRoute,
  ifrs16LeaseContractCreationInitiatedFromList,
  ifrs16LeaseContractCreationWorkflowInitiatedFromList,
  ifrs16LeaseContractDataHasMutated,
  ifrs16LeaseContractEditWorkflowInitiatedEnRoute,
  ifrs16LeaseContractEditWorkflowInitiatedFromList,
  ifrs16LeaseContractEditWorkflowInitiatedFromView,
  ifrs16LeaseContractUpdateFormHasBeenDestroyed
} from '../actions/ifrs16-lease-model-update-status.actions';

export const ifrs16LeaseModelUpdateFormStateSelector = 'ifrs16LeaseModelUpdateForm';

export interface IFRS16LeaseModelFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IIFRS16LeaseContract;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _ifrs16LeaseModelUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(ifrs16LeaseContractCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(ifrs16LeaseContractCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(ifrs16LeaseContractEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractDataHasMutated, (state) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(ifrs16LeaseContractCreationInitiatedFromList, (state) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(ifrs16LeaseContractCreationInitiatedEnRoute, (state) => ({
    ...state,
    ifrs16LeaseModelFormState: {
      ...state.ifrs16LeaseModelFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function ifrs16LeaseModelUpdateStateReducer(state: State = initialState, action: Action): State {

  return _ifrs16LeaseModelUpdateStateReducer(state, action);
}
