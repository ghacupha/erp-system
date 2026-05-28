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
import { ITransactionAccountPostingRule } from '../../erp-accounts/transaction-account-posting-rule/transaction-account-posting-rule.model';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import {
  leasePostingRuleCopyWorkflowInitiatedEnRoute,
  leasePostingRuleCopyWorkflowInitiatedFromList,
  leasePostingRuleCreationInitiatedEnRoute,
  leasePostingRuleCreationWorkflowInitiatedFromList,
  leasePostingRuleDataHasMutated,
  leasePostingRuleDeleteWorkflowInitiatedFromList,
  leasePostingRuleEditWorkflowInitiatedEnRoute,
  leasePostingRuleEditWorkflowInitiatedFromList,
  leasePostingRuleUpdateFormHasBeenDestroyed,
  leasePostingRuleUpdateInstanceAcquiredFromBackend,
} from '../actions/lease-posting-rule-workflow-status.actions';

export const leasePostingRuleUpdateFormStateSelector = 'leasePostingRuleUpdateForm';

export interface LeasePostingRuleFormState {
  selectedInstance: ITransactionAccountPostingRule;
  sourceLeaseContract: IIFRS16LeaseContract | null;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
  weAreDeleting: boolean;
}

const _leasePostingRuleUpdateStateReducer = createReducer(
  initialState,
  on(leasePostingRuleCreationWorkflowInitiatedFromList, state => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: {},
      sourceLeaseContract: null,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleCreationInitiatedEnRoute, state => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: {},
      sourceLeaseContract: null,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleCopyWorkflowInitiatedFromList, (state, { copiedInstance }) => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleCopyWorkflowInitiatedEnRoute, (state, { copiedInstance }) => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: copiedInstance,
      weAreCopying: true,
      weAreEditing: false,
      weAreCreating: false,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleEditWorkflowInitiatedFromList, (state, { editedInstance }) => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleEditWorkflowInitiatedEnRoute, (state, { editedInstance }) => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: editedInstance,
      weAreCopying: false,
      weAreEditing: true,
      weAreCreating: false,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleDeleteWorkflowInitiatedFromList, (state, { deletedInstance }) => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: deletedInstance,
      weAreDeleting: true,
    },
  })),
  on(leasePostingRuleUpdateInstanceAcquiredFromBackend, (state, { backendAcquiredInstance, sourceLeaseContract }) => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: backendAcquiredInstance,
      sourceLeaseContract: sourceLeaseContract ?? state.leasePostingRuleFormState.sourceLeaseContract,
    },
  })),
  on(leasePostingRuleUpdateFormHasBeenDestroyed, state => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: {},
      sourceLeaseContract: null,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
      weAreDeleting: false,
    },
  })),
  on(leasePostingRuleDataHasMutated, state => ({
    ...state,
    leasePostingRuleFormState: {
      ...state.leasePostingRuleFormState,
      selectedInstance: {},
      sourceLeaseContract: null,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: false,
      weAreDeleting: false,
    },
  }))
);

export function leasePostingRuleUpdateStateReducer(state: State = initialState, action: Action): State {
  return _leasePostingRuleUpdateStateReducer(state, action);
}
