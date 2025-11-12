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
import { ILeaseAmortizationCalculation } from '../../erp-leases/lease-amortization-calculation/lease-amortization-calculation.model';
import {
  leaseAmortizationCalculationCopyWorkflowInitiatedEnRoute,
  leaseAmortizationCalculationCopyWorkflowInitiatedFromList,
  leaseAmortizationCalculationCopyWorkflowInitiatedFromView,
  leaseAmortizationCalculationCreationInitiatedEnRoute,
  leaseAmortizationCalculationCreationInitiatedFromList,
  leaseAmortizationCalculationCreationWorkflowInitiatedFromList,
  leaseAmortizationCalculationEditWorkflowInitiatedEnRoute,
  leaseAmortizationCalculationEditWorkflowInitiatedFromList,
  leaseAmortizationCalculationEditWorkflowInitiatedFromView,
} from '../actions/lease-amortization-calculation.actions';

export const leaseAmortizationCalculationFormStateSelector = 'leaseAmortizationCalculationForm';

export interface LeaseAmortizationCalculationFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ILeaseAmortizationCalculation;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _leaseAmortizationCalculationStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(leaseAmortizationCalculationCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(leaseAmortizationCalculationCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(leaseAmortizationCalculationCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(leaseAmortizationCalculationCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(leaseAmortizationCalculationEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(leaseAmortizationCalculationEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(leaseAmortizationCalculationEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(leaseAmortizationCalculationCreationInitiatedFromList, (state) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(leaseAmortizationCalculationCreationInitiatedEnRoute, (state) => ({
    ...state,
    leaseAmortizationCalculationFormState: {
      ...state.leaseAmortizationCalculationFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function leaseAmortizationCalculationStateReducer(state: State = initialState, action: Action): State {

  return _leaseAmortizationCalculationStateReducer(state, action);
}
