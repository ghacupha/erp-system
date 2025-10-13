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
import {
  prepaymentAccountCopyWorkflowInitiatedEnRoute,
  prepaymentAccountCopyWorkflowInitiatedFromList,
  prepaymentAccountCopyWorkflowInitiatedFromView,
  prepaymentAccountCreationInitiatedEnRoute,
  prepaymentAccountCreationInitiatedFromList,
  prepaymentAccountCreationWorkflowInitiatedFromList,
  prepaymentAccountDataHasMutated,
  prepaymentAccountEditWorkflowInitiatedEnRoute,
  prepaymentAccountEditWorkflowInitiatedFromList,
  prepaymentAccountEditWorkflowInitiatedFromView,
  prepaymentAccountUpdateFormHasBeenDestroyed
} from '../actions/prepayment-account-update-status.actions';
import { IPrepaymentAccount } from '../../erp-prepayments/prepayment-account/prepayment-account.model';

export const prepaymentAccountUpdateFormStateSelector = 'prepaymentAccountUpdateForm';

export interface PrepaymentAccountFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: IPrepaymentAccount;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _prepaymentAccountUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(prepaymentAccountCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(prepaymentAccountCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(prepaymentAccountEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountDataHasMutated, (state) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(prepaymentAccountCreationInitiatedFromList, (state) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(prepaymentAccountCreationInitiatedEnRoute, (state) => ({
    ...state,
    prepaymentAccountFormState: {
      ...state.prepaymentAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function prepaymentAccountUpdateStateReducer(state: State = initialState, action: Action): State {

  return _prepaymentAccountUpdateStateReducer(state, action);
}
