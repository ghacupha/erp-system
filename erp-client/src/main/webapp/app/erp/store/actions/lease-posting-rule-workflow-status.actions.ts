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

import { createAction, props } from '@ngrx/store';
import { ITransactionAccountPostingRule } from '../../erp-accounts/transaction-account-posting-rule/transaction-account-posting-rule.model';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export const leasePostingRuleCreationWorkflowInitiatedFromList = createAction(
  '[LeasePostingRule Create: List] Lease posting rule create workflow initiated'
);

export const leasePostingRuleCreationInitiatedEnRoute = createAction(
  '[LeasePostingRule Create: Route] Lease posting rule create workflow initiated'
);

export const leasePostingRuleEditWorkflowInitiatedFromList = createAction(
  '[LeasePostingRule Edit: List] Lease posting rule edit workflow initiated',
  props<{ editedInstance: ITransactionAccountPostingRule }>()
);

export const leasePostingRuleEditWorkflowInitiatedEnRoute = createAction(
  '[LeasePostingRule Edit: Route] Lease posting rule edit workflow initiated',
  props<{ editedInstance: ITransactionAccountPostingRule }>()
);

export const leasePostingRuleCopyWorkflowInitiatedFromList = createAction(
  '[LeasePostingRule Copy: List] Lease posting rule copy workflow initiated',
  props<{ copiedInstance: ITransactionAccountPostingRule }>()
);

export const leasePostingRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[LeasePostingRule Copy: Route] Lease posting rule copy workflow initiated',
  props<{ copiedInstance: ITransactionAccountPostingRule }>()
);

export const leasePostingRuleDeleteWorkflowInitiatedFromList = createAction(
  '[LeasePostingRule Delete: List] Lease posting rule delete workflow initiated',
  props<{ deletedInstance: ITransactionAccountPostingRule }>()
);

export const leasePostingRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[LeasePostingRule Effects] Lease posting rule update instance acquired',
  props<{ backendAcquiredInstance: ITransactionAccountPostingRule; sourceLeaseContract?: IIFRS16LeaseContract | null }>()
);

export const leasePostingRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[LeasePostingRule Effects] Lease posting rule update instance acquisition failed',
  props<{ error: unknown }>()
);

export const leasePostingRuleUpdateFormHasBeenDestroyed = createAction(
  '[LeasePostingRule Form] Lease posting rule update form destroyed'
);

export const leasePostingRuleDataHasMutated = createAction('[LeasePostingRule Form] Lease posting rule data mutated');
