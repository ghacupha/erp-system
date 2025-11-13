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

import { IRouModelMetadata } from '../../erp-leases/rou-model-metadata/rou-model-metadata.model';
import { initialState, State } from '../global-store.definition';
import { Action, createReducer, on } from '@ngrx/store';
import {
  rouMetadataCopyWorkflowInitiatedEnRoute,
  rouMetadataCopyWorkflowInitiatedFromList,
  rouMetadataCopyWorkflowInitiatedFromView,
  rouMetadataCreationInitiatedEnRoute,
  rouMetadataCreationInitiatedFromList,
  rouMetadataCreationWorkflowInitiatedFromList,
  rouMetadataDataHasMutated,
  rouMetadataEditWorkflowInitiatedEnRoute,
  rouMetadataEditWorkflowInitiatedFromList,
  rouMetadataEditWorkflowInitiatedFromView,
  rouMetadataUpdateFormHasBeenDestroyed
} from '../actions/rou-model-metadata-update-status.actions';

export const rouModelMetadataUpdateFormStateSelector = 'rouModelMetadataUpdateForm';

export interface RouModelMetadataFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IRouModelMetadata;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _rouModelMetadataUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(rouMetadataCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(rouMetadataCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouMetadataCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouMetadataCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(rouMetadataEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(rouMetadataEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(rouMetadataEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(rouMetadataUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouMetadataDataHasMutated, (state) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(rouMetadataCreationInitiatedFromList, (state) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(rouMetadataCreationInitiatedEnRoute, (state) => ({
    ...state,
    rouModelMetadataFormState: {
      ...state.rouModelMetadataFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function rouModelMetadataUpdateStateReducer(state: State = initialState, action: Action): State {

  return _rouModelMetadataUpdateStateReducer(state, action);
}
