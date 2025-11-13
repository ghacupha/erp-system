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
import { IPrepaymentMarshalling } from '../../erp-prepayments/prepayment-marshalling/prepayment-marshalling.model';
import {
  prepaymentMarshallingCopyWorkflowInitiatedEnRoute,
  prepaymentMarshallingCopyWorkflowInitiatedFromList,
  prepaymentMarshallingCopyWorkflowInitiatedFromView,
  prepaymentMarshallingCreationInitiatedEnRoute,
  prepaymentMarshallingCreationInitiatedFromList,
  prepaymentMarshallingCreationWorkflowInitiatedFromList,
  prepaymentMarshallingDataHasMutated,
  prepaymentMarshallingEditWorkflowInitiatedEnRoute,
  prepaymentMarshallingEditWorkflowInitiatedFromList,
  prepaymentMarshallingEditWorkflowInitiatedFromView,
  prepaymentMarshallingUpdateFormHasBeenDestroyed
} from '../actions/prepayment-marshalling-update-status.actions';

export const prepaymentMarshallingUpdateFormStateSelector = 'prepaymentMarshallingUpdateForm';

export interface PrepaymentMarshallingFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IPrepaymentMarshalling;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _prepaymentMarshallingUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(prepaymentMarshallingCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(prepaymentMarshallingCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(prepaymentMarshallingEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingDataHasMutated, (state) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentMarshallingCreationInitiatedFromList, (state) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(prepaymentMarshallingCreationInitiatedEnRoute, (state) => ({
    ...state,
    prepaymentMarshallingFormState: {
      ...state.prepaymentMarshallingFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function prepaymentMarshallingUpdateStateReducer(state: State = initialState, action: Action): State {

  return _prepaymentMarshallingUpdateStateReducer(state, action);
}
