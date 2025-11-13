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
import { ITARecognitionROURule } from '../../erp-accounts/ta-recognition-rou-rule/ta-recognition-rou-rule.model';

export const taRecognitionRouRuleCreationInitiatedFromList = createAction(
  '[TARecognitionROURule Creation: List] TA Recognition ROU Rule creation workflow initiated',
);

export const taRecognitionRouRuleCreationInitiatedEnRoute = createAction(
  '[TARecognitionROURule: Route] TA Recognition ROU Rule create workflow initiated',
);

export const taRecognitionRouRuleCreationWorkflowInitiatedFromList = createAction(
  '[TARecognitionROURule Create: List] TA Recognition ROU Rule create workflow initiated',
);

export const taRecognitionRouRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[TARecognitionROURule Copy: Route] TA Recognition ROU Rule copy workflow initiated',
  props<{ copiedInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleCopyWorkflowInitiatedFromList = createAction(
  '[TARecognitionROURule Copy: List] TA Recognition ROU Rule copy workflow initiated',
  props<{ copiedInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleCopyWorkflowInitiatedFromView = createAction(
  '[TARecognitionROURule Copy: View] TA Recognition ROU Rule copy workflow initiated',
  props<{ copiedInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleEditWorkflowInitiatedEnRoute = createAction(
  '[TARecognitionROURule Edit: Route] TA Recognition ROU Rule edit workflow initiated',
  props<{ editedInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleEditWorkflowInitiatedFromList = createAction(
  '[TARecognitionROURule Edit: List] TA Recognition ROU Rule edit workflow initiated',
  props<{ editedInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleEditWorkflowInitiatedFromView = createAction(
  '[TARecognitionROURule Edit: View] TA Recognition ROU Rule edit workflow initiated',
  props<{ editedInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleUpdateFormHasBeenDestroyed = createAction(
  '[TARecognitionROURule Form] TA Recognition ROU Rule form destroyed',
);

export const taRecognitionRouRuleDataHasMutated = createAction(
  '[TARecognitionROURule Form] TA Recognition ROU Rule form data mutated',
);

export const taRecognitionRouRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[TARecognitionROURuleEffects: Copied-TARecognitionROU-effects] TA Recognition ROU Rule instance acquired for copy',
  props<{ backendAcquiredInstance: ITARecognitionROURule }>()
);

export const taRecognitionRouRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[TARecognitionROURuleEffects: Copied-TARecognitionROU-effects] TA Recognition ROU Rule instance acquisition failed',
  props<{ error: string }>()
);
