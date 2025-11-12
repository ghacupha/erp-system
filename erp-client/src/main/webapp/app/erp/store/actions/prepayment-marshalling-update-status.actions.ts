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
import { IPrepaymentMarshalling } from '../../erp-prepayments/prepayment-marshalling/prepayment-marshalling.model';

export const prepaymentMarshallingCreationInitiatedFromList = createAction(
  '[PrepaymentMarshalling Creation: List] Prepayment Marshalling creation workflow initiated',
);

export const prepaymentMarshallingCopyWorkflowInitiatedEnRoute = createAction(
  '[PrepaymentMarshalling Copy: Route] Prepayment Marshalling copy workflow initiated',
  props<{ copiedInstance: IPrepaymentMarshalling }>()
);

export const prepaymentMarshallingCopyWorkflowInitiatedFromList = createAction(
  '[PrepaymentMarshalling Copy: List] Prepayment Marshalling copy workflow initiated',
  props<{ copiedInstance: IPrepaymentMarshalling }>()
);

export const prepaymentMarshallingCopyWorkflowInitiatedFromView = createAction(
  '[PrepaymentMarshalling Copy: View] Prepayment Marshalling copy workflow initiated',
  props<{ copiedInstance: IPrepaymentMarshalling }>()
);

export const prepaymentMarshallingEditWorkflowInitiatedEnRoute = createAction(
  '[PrepaymentMarshalling Edit: Route] Prepayment Marshalling edit workflow initiated',
  props<{ editedInstance: IPrepaymentMarshalling }>()
);

export const prepaymentMarshallingEditWorkflowInitiatedFromList = createAction(
  '[PrepaymentMarshalling Edit: List] Prepayment Marshalling edit workflow initiated',
  props<{ editedInstance: IPrepaymentMarshalling }>()
);

export const prepaymentMarshallingEditWorkflowInitiatedFromView = createAction(
  '[PrepaymentMarshalling Edit: View] Prepayment Marshalling edit workflow initiated',
  props<{ editedInstance: IPrepaymentMarshalling }>()
);

export const prepaymentMarshallingCreationInitiatedEnRoute = createAction(
  '[PrepaymentMarshalling: Route] Prepayment Marshalling create workflow initiated',
);

export const prepaymentMarshallingCreationWorkflowInitiatedFromList = createAction(
  '[PrepaymentMarshalling Create: List] Prepayment Marshalling create workflow initiated',
);

export const prepaymentMarshallingUpdateFormHasBeenDestroyed = createAction(
  '[PrepaymentMarshalling Form] Prepayment Marshalling form destroyed',
);

export const prepaymentMarshallingDataHasMutated = createAction(
  '[PrepaymentMarshalling Form] Prepayment Marshalling form data mutated',
);

export const prepaymentMarshallingUpdateInstanceAcquiredFromBackend = createAction(
  '[PrepaymentMarshallingEffects: Copied-Prepayment-Marshalling-effects] prepayment-marshalling-update instance acquired for copy',
  props<{backendAcquiredInstance: IPrepaymentMarshalling}>()
);

export const prepaymentMarshallingUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[PrepaymentMarshallingEffects: Copied-Prepayment-Marshalling-effects] prepayment-marshalling-update instance acquisition failed',
  props<{error: string}>()
);
