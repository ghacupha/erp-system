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

import { ILeaseTemplate } from '../../erp-leases/lease-template/lease-template.model';
import { initialState, State } from '../global-store.definition';
import { Action, createReducer, on } from '@ngrx/store';
import {
  leaseTemplateCopyWorkflowInitiatedEnRoute,
  leaseTemplateCopyWorkflowInitiatedFromList,
  leaseTemplateCopyWorkflowInitiatedFromView,
  leaseTemplateCreationInitiatedEnRoute,
  leaseTemplateCreationInitiatedFromList,
  leaseTemplateCreationWorkflowInitiatedFromList,
  leaseTemplateDataHasMutated,
  leaseTemplateEditWorkflowInitiatedEnRoute,
  leaseTemplateEditWorkflowInitiatedFromList,
  leaseTemplateEditWorkflowInitiatedFromView,
  leaseTemplateUpdateFormHasBeenDestroyed
} from '../actions/lease-template-update-status.actions';

export const leaseTemplateUpdateFormStateSelector = 'leaseTemplateUpdateForm';

export interface LeaseTemplateFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ILeaseTemplate;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _leaseTemplateUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(leaseTemplateCreationWorkflowInitiatedFromList, state => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    },
  })),

  // workflows for copy
  on(leaseTemplateCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateCopyWorkflowInitiatedFromView, (state, { copiedInstance }) => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    },
  })),

  // workflows for edit
  on(leaseTemplateEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateEditWorkflowInitiatedFromView, (state, { editedInstance }) => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateUpdateFormHasBeenDestroyed, state => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateDataHasMutated, state => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    },
  })),

  on(leaseTemplateCreationInitiatedFromList, state => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    },
  })),

  on(leaseTemplateCreationInitiatedEnRoute, state => ({
    ...state,
    leaseTemplateFormState: {
      ...state.leaseTemplateFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    },
  })),
);

export function leaseTemplateUpdateStateReducer(state: State = initialState, action: Action): State {
  return _leaseTemplateUpdateStateReducer(state, action);
}
