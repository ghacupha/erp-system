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
import { ITALeaseRecognitionRule } from '../../erp-accounts/ta-lease-recognition-rule/ta-lease-recognition-rule.model';

export const taLeaseRecognitionRuleCreationInitiatedFromList = createAction(
  '[TALeaseRecognitionRule Creation: List] TA Lease Recognition Rule creation workflow initiated',
);

export const taLeaseRecognitionRuleCreationInitiatedEnRoute = createAction(
  '[TALeaseRecognitionRule: Route] TA Lease Recognition Rule create workflow initiated',
);

export const taLeaseRecognitionRuleCreationWorkflowInitiatedFromList = createAction(
  '[TALeaseRecognitionRule Create: List] TA Lease Recognition Rule create workflow initiated',
);

export const taLeaseRecognitionRuleCopyWorkflowInitiatedEnRoute = createAction(
  '[TALeaseRecognitionRule Copy: Route] TA Lease Recognition Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleCopyWorkflowInitiatedFromList = createAction(
  '[TALeaseRecognitionRule Copy: List] TA Lease Recognition Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleCopyWorkflowInitiatedFromView = createAction(
  '[TALeaseRecognitionRule Copy: View] TA Lease Recognition Rule copy workflow initiated',
  props<{ copiedInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleEditWorkflowInitiatedEnRoute = createAction(
  '[TALeaseRecognitionRule Edit: Route] TA Lease Recognition Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleEditWorkflowInitiatedFromList = createAction(
  '[TALeaseRecognitionRule Edit: List] TA Lease Recognition Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleEditWorkflowInitiatedFromView = createAction(
  '[TALeaseRecognitionRule Edit: View] TA Lease Recognition Rule edit workflow initiated',
  props<{ editedInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleUpdateFormHasBeenDestroyed = createAction(
  '[TALeaseRecognitionRule Form] TA Lease Recognition Rule form destroyed',
);

export const taLeaseRecognitionRuleDataHasMutated = createAction(
  '[TALeaseRecognitionRule Form] TA Lease Recognition Rule form data mutated',
);

export const taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend = createAction(
  '[TALeaseRecognitionRuleEffects: Copied-TALeaseRecognition-effects] TA Lease Recognition Rule instance acquired for copy',
  props<{ backendAcquiredInstance: ITALeaseRecognitionRule }>()
);

export const taLeaseRecognitionRuleUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[TALeaseRecognitionRuleEffects: Copied-TALeaseRecognition-effects] TA Lease Recognition Rule instance acquisition failed',
  props<{ error: string }>()
);
