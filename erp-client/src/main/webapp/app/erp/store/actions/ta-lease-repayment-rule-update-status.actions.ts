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
import { ITALeaseRepaymentRule } from '../../erp-accounts/ta-lease-repayment-rule/ta-lease-repayment-rule.model';

export const taLeaseRepaymentRuleCreationInitiatedFromList = createAction(
  '[TALeaseRepaymentRule Creation: List] TA Lease Repayment Rule creation workflow initiated',
);

export const taLeaseRepaymentRuleCreationInitiatedEnRoute = createAction(
  '[TALeaseRepaymentRule: Route] TA Lease Repayment Rule create workflow initiated',
);

export const taLeaseRepaymentRuleCreationWorkflowInitiatedFromList = createAction(
  '[TALeaseRepaymentRule Create: List] TA Lease Repayment Rule create workflow initiated',
);

export const taLeaseRepaymentRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[TALeaseRepaymentRule Copy: Route] TA Lease Repayment Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleCopyWorkflowInitiatedFromList = createAction(
  '[TALeaseRepaymentRule Copy: List] TA Lease Repayment Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleCopyWorkflowInitiatedFromView = createAction(
  '[TALeaseRepaymentRule Copy: View] TA Lease Repayment Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleEditWorkflowInitiatedEnRoute = createAction(
  '[TALeaseRepaymentRule Edit: Route] TA Lease Repayment Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleEditWorkflowInitiatedFromList = createAction(
  '[TALeaseRepaymentRule Edit: List] TA Lease Repayment Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleEditWorkflowInitiatedFromView = createAction(
  '[TALeaseRepaymentRule Edit: View] TA Lease Repayment Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleUpdateFormHasBeenDestroyed = createAction(
  '[TALeaseRepaymentRule Form] TA Lease Repayment Rule form destroyed',
);

export const taLeaseRepaymentRuleDataHasMutated = createAction(
  '[TALeaseRepaymentRule Form] TA Lease Repayment Rule form data mutated',
);

export const taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[TALeaseRepaymentRuleEffects: Copied-TALeaseRepayment-effects] TA Lease Repayment Rule instance acquired for copy',
  props<{ backendAcquiredInstance: ITALeaseRepaymentRule }>()
);

export const taLeaseRepaymentRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[TALeaseRepaymentRuleEffects: Copied-TALeaseRepayment-effects] TA Lease Repayment Rule instance acquisition failed',
  props<{ error: string }>()
);
