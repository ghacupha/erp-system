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

import { createAction, props } from '@ngrx/store';
import { ITALeaseInterestAccrualRule } from '../../erp-accounts/ta-lease-interest-accrual-rule/ta-lease-interest-accrual-rule.model';

export const taLeaseInterestAccrualRuleCreationInitiatedFromList = createAction(
  '[TALeaseInterestAccrualRule Creation: List] TA Lease Interest Accrual Rule creation workflow initiated',
);

export const taLeaseInterestAccrualRuleCreationInitiatedEnRoute = createAction(
  '[TALeaseInterestAccrualRule: Route] TA Lease Interest Accrual Rule create workflow initiated',
);

export const taLeaseInterestAccrualRuleCreationWorkflowInitiatedFromList = createAction(
  '[TALeaseInterestAccrualRule Create: List] TA Lease Interest Accrual Rule create workflow initiated',
);

export const taLeaseInterestAccrualRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[TALeaseInterestAccrualRule Copy: Route] TA Lease Interest Accrual Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList = createAction(
  '[TALeaseInterestAccrualRule Copy: List] TA Lease Interest Accrual Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromView = createAction(
  '[TALeaseInterestAccrualRule Copy: View] TA Lease Interest Accrual Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleEditWorkflowInitiatedEnRoute = createAction(
  '[TALeaseInterestAccrualRule Edit: Route] TA Lease Interest Accrual Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleEditWorkflowInitiatedFromList = createAction(
  '[TALeaseInterestAccrualRule Edit: List] TA Lease Interest Accrual Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView = createAction(
  '[TALeaseInterestAccrualRule Edit: View] TA Lease Interest Accrual Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed = createAction(
  '[TALeaseInterestAccrualRule Form] TA Lease Interest Accrual Rule form destroyed',
);

export const taLeaseInterestAccrualRuleDataHasMutated = createAction(
  '[TALeaseInterestAccrualRule Form] TA Lease Interest Accrual Rule form data mutated',
);

export const taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[TALeaseInterestAccrualRuleEffects: Copied-TALeaseInterestAccrual-effects] TA Lease Interest Accrual Rule instance acquired for copy',
  props<{ backendAcquiredInstance: ITALeaseInterestAccrualRule }>()
);

export const taLeaseInterestAccrualRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[TALeaseInterestAccrualRuleEffects: Copied-TALeaseInterestAccrual-effects] TA Lease Interest Accrual Rule instance acquisition failed',
  props<{ error: string }>()
);
