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
import { IWorkInProgressRegistration } from '../../erp-assets/work-in-progress-registration/work-in-progress-registration.model';
import {
  wipRegistrationCopyWorkflowInitiatedEnRoute,
  wipRegistrationCopyWorkflowInitiatedFromList,
  wipRegistrationCopyWorkflowInitiatedFromView,
  wipRegistrationCreationInitiatedEnRoute,
  wipRegistrationCreationInitiatedFromList,
  wipRegistrationCreationWorkflowInitiatedFromList,
  wipRegistrationDataHasMutated,
  wipRegistrationEditWorkflowInitiatedEnRoute,
  wipRegistrationEditWorkflowInitiatedFromList,
  wipRegistrationEditWorkflowInitiatedFromView,
  wipRegistrationUpdateFormHasBeenDestroyed
} from '../actions/wip-registration-update-status.actions';

export const wipRegistrationUpdateFormStateSelector = 'wipRegistrationUpdateForm';

export interface WIPRegistrationFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IWorkInProgressRegistration;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _wipRegistrationUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(wipRegistrationCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(wipRegistrationCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(wipRegistrationEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationDataHasMutated, (state) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(wipRegistrationCreationInitiatedFromList, (state) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(wipRegistrationCreationInitiatedEnRoute, (state) => ({
    ...state,
    wipRegistrationFormState: {
      ...state.wipRegistrationFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function wipRegistrationUpdateStateReducer(state: State = initialState, action: Action): State {

  return _wipRegistrationUpdateStateReducer(state, action);
}
