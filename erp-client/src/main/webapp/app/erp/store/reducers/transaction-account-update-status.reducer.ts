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
import { ITransactionAccount } from '../../erp-accounts/transaction-account/transaction-account.model';
import {
  transactionAccountCopyWorkflowInitiatedEnRoute,
  transactionAccountCopyWorkflowInitiatedFromList,
  transactionAccountCopyWorkflowInitiatedFromView,
  transactionAccountCreationInitiatedEnRoute,
  transactionAccountCreationInitiatedFromList,
  transactionAccountCreationWorkflowInitiatedFromList,
  transactionAccountDataHasMutated,
  transactionAccountEditWorkflowInitiatedEnRoute,
  transactionAccountEditWorkflowInitiatedFromList,
  transactionAccountEditWorkflowInitiatedFromView,
  transactionAccountUpdateFormHasBeenDestroyed
} from '../actions/transaction-account-update-status.actions';

export const transactionAccountUpdateFormStateSelector = 'transactionAccountUpdateForm';

export interface TransactionAccountFormState {
  backEndFetchComplete: boolean;
  browserHasBeenRefreshed: boolean;
  selectedInstance: ITransactionAccount;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _transactionAccountUpdateStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(transactionAccountCreationWorkflowInitiatedFromList, (state) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  //    workflows for copy
  on(transactionAccountCopyWorkflowInitiatedEnRoute, (state, {copiedInstance}) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(transactionAccountCopyWorkflowInitiatedFromView, (state, {copiedInstance}) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(transactionAccountCopyWorkflowInitiatedFromList, (state, {copiedInstance}) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),


  //    workflows for edit workflows
  on(transactionAccountEditWorkflowInitiatedEnRoute, (state, {editedInstance}) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(transactionAccountEditWorkflowInitiatedFromView, (state, {editedInstance}) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(transactionAccountEditWorkflowInitiatedFromList, (state, {editedInstance}) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
    }
  })),

  on(transactionAccountUpdateFormHasBeenDestroyed, (state) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(transactionAccountDataHasMutated, (state) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
    }
  })),

  on(transactionAccountCreationInitiatedFromList, (state) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  on(transactionAccountCreationInitiatedEnRoute, (state) => ({
    ...state,
    transactionAccountFormState: {
      ...state.transactionAccountFormState,
      selectedInstance: {},
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),
);

export function transactionAccountUpdateStateReducer(state: State = initialState, action: Action): State {

  return _transactionAccountUpdateStateReducer(state, action);
}
