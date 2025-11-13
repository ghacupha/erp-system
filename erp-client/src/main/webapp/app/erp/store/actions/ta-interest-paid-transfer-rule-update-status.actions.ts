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
import { ITAInterestPaidTransferRule } from '../../erp-accounts/ta-interest-paid-transfer-rule/ta-interest-paid-transfer-rule.model';

export const taInterestPaidTransferRuleCreationInitiatedFromList = createAction(
  '[TAInterestPaidTransferRule Creation: List] TA Interest Paid Transfer Rule creation workflow initiated',
);

export const taInterestPaidTransferRuleCreationInitiatedEnRoute = createAction(
  '[TAInterestPaidTransferRule: Route] TA Interest Paid Transfer Rule create workflow initiated',
);

export const taInterestPaidTransferRuleCreationWorkflowInitiatedFromList = createAction(
  '[TAInterestPaidTransferRule Create: List] TA Interest Paid Transfer Rule create workflow initiated',
);

export const taInterestPaidTransferRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[TAInterestPaidTransferRule Copy: Route] TA Interest Paid Transfer Rule copy workflow initiated',
  props<{ copiedInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleCopyWorkflowInitiatedFromList = createAction(
  '[TAInterestPaidTransferRule Copy: List] TA Interest Paid Transfer Rule copy workflow initiated',
  props<{ copiedInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleCopyWorkflowInitiatedFromView = createAction(
  '[TAInterestPaidTransferRule Copy: View] TA Interest Paid Transfer Rule copy workflow initiated',
  props<{ copiedInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleEditWorkflowInitiatedEnRoute = createAction(
  '[TAInterestPaidTransferRule Edit: Route] TA Interest Paid Transfer Rule edit workflow initiated',
  props<{ editedInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleEditWorkflowInitiatedFromList = createAction(
  '[TAInterestPaidTransferRule Edit: List] TA Interest Paid Transfer Rule edit workflow initiated',
  props<{ editedInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleEditWorkflowInitiatedFromView = createAction(
  '[TAInterestPaidTransferRule Edit: View] TA Interest Paid Transfer Rule edit workflow initiated',
  props<{ editedInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleUpdateFormHasBeenDestroyed = createAction(
  '[TAInterestPaidTransferRule Form] TA Interest Paid Transfer Rule form destroyed',
);

export const taInterestPaidTransferRuleDataHasMutated = createAction(
  '[TAInterestPaidTransferRule Form] TA Interest Paid Transfer Rule form data mutated',
);

export const taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[TAInterestPaidTransferRuleEffects: Copied-TA-Interest-Paid-Transfer-effects] TA Interest Paid Transfer Rule instance acquired for copy',
  props<{ backendAcquiredInstance: ITAInterestPaidTransferRule }>()
);

export const taInterestPaidTransferRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[TAInterestPaidTransferRuleEffects: Copied-TA-Interest-Paid-Transfer-effects] TA Interest Paid Transfer Rule instance acquisition failed',
  props<{ error: string }>()
);
