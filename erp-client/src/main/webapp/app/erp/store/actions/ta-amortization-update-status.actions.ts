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
import { ITAAmortizationRule } from '../../erp-accounts/ta-amortization-rule/ta-amortization-rule.model';

export const taAmortizationRuleCreationInitiatedFromList = createAction(
  '[TAAmortizationRule Creation: List] TA Amortization Rule creation workflow initiated',
);

export const taAmortizationRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[TAAmortizationRule Copy: Route] TA Amortization Rule copy workflow initiated',
  props<{ copiedInstance: ITAAmortizationRule }>()
);

export const taAmortizationRuleCopyWorkflowInitiatedFromList = createAction(
  '[TAAmortizationRule Copy: List] TA Amortization Rule copy workflow initiated',
  props<{ copiedInstance: ITAAmortizationRule }>()
);

export const taAmortizationRuleCopyWorkflowInitiatedFromView = createAction(
  '[TAAmortizationRule Copy: View] TA Amortization Rule copy workflow initiated',
  props<{ copiedInstance: ITAAmortizationRule }>()
);

export const taAmortizationRuleEditWorkflowInitiatedEnRoute = createAction(
  '[TAAmortizationRule Edit: Route] TA Amortization Rule edit workflow initiated',
  props<{ editedInstance: ITAAmortizationRule }>()
);

export const taAmortizationRuleEditWorkflowInitiatedFromList = createAction(
  '[TAAmortizationRule Edit: List] TA Amortization Rule edit workflow initiated',
  props<{ editedInstance: ITAAmortizationRule }>()
);

export const taAmortizationRuleEditWorkflowInitiatedFromView = createAction(
  '[TAAmortizationRule Edit: View] TA Amortization Rule edit workflow initiated',
  props<{ editedInstance: ITAAmortizationRule }>()
);

export const taAmortizationRuleCreationInitiatedEnRoute = createAction(
  '[TAAmortizationRule: Route] TA Amortization Rule create workflow initiated',
);

export const taAmortizationRuleCreationWorkflowInitiatedFromList = createAction(
  '[TAAmortizationRule Create: List] TA Amortization Rule create workflow initiated',
);

export const taAmortizationRuleUpdateFormHasBeenDestroyed = createAction(
  '[TAAmortizationRule Form] TA Amortization Rule form destroyed',
);

export const taAmortizationRuleDataHasMutated = createAction(
  '[TAAmortizationRule Form] TA Amortization Rule form data mutated',
);

export const taAmortizationRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[TAAmortizationRuleEffects: Copied-TA-Amortization-effects] lease-contract-update instance acquired for copy',
  props<{backendAcquiredInstance: ITAAmortizationRule}>()
);

export const taAmortizationRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[TAAmortizationRuleEffects: Copied-TA-Amortization-effects] lease-contract-update instance acquisition failed',
  props<{error: string}>()
);
