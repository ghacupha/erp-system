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
import { ILeaseLiability } from '../../erp-leases/lease-liability/lease-liability.model';
import {
  leaseLiabilityCopyWorkflowInitiatedEnRoute,
  leaseLiabilityCopyWorkflowInitiatedFromList,
  leaseLiabilityCopyWorkflowInitiatedFromView,
  leaseLiabilityCreationInitiatedEnRoute,
  leaseLiabilityCreationInitiatedFromList,
  leaseLiabilityCreationWorkflowInitiatedFromList,
  leaseLiabilityEditWorkflowInitiatedEnRoute,
  leaseLiabilityEditWorkflowInitiatedFromList,
  leaseLiabilityEditWorkflowInitiatedFromView
} from '../actions/lease-liability.actions';

export const leaseLiabilityFormStateSelector = 'leaseLiabilityForm';

export interface LeaseLiabilityFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ILeaseLiability;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _leaseLiabilityStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(leaseLiabilityCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(leaseLiabilityCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(leaseLiabilityCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(leaseLiabilityCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(leaseLiabilityEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(leaseLiabilityEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(leaseLiabilityEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(leaseLiabilityCreationInitiatedFromList, (state) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(leaseLiabilityCreationInitiatedEnRoute, (state) => ({
    ...state,
    leaseLiabilityFormState: {
      ...state.leaseLiabilityFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function leaseLiabilityStateReducer(state: State = initialState, action: Action): State {

  return _leaseLiabilityStateReducer(state, action);
}
