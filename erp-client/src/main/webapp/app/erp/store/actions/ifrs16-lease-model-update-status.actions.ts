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
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export const ifrs16LeaseContractCreationInitiatedFromList = createAction(
  '[IFRS16LeaseContract Creation: List] IFRS16 Lease Contract creation workflow initiated',
);

export const ifrs16LeaseContractCopyWorkflowInitiatedEnRoute = createAction(
  '[IFRS16LeaseContract Copy: Route] IFRS16 Lease Contract copy workflow initiated',
  props<{ copiedInstance: IIFRS16LeaseContract }>()
);

export const ifrs16LeaseContractCopyWorkflowInitiatedFromList = createAction(
  '[IFRS16LeaseContract Copy: List] IFRS16 Lease Contract copy workflow initiated',
  props<{ copiedInstance: IIFRS16LeaseContract }>()
);

export const ifrs16LeaseContractCopyWorkflowInitiatedFromView = createAction(
  '[IFRS16LeaseContract Copy: View] IFRS16 Lease Contract copy workflow initiated',
  props<{ copiedInstance: IIFRS16LeaseContract }>()
);

export const ifrs16LeaseContractEditWorkflowInitiatedEnRoute = createAction(
  '[IFRS16LeaseContract Edit: Route] IFRS16 Lease Contract edit workflow initiated',
  props<{ editedInstance: IIFRS16LeaseContract }>()
);

export const ifrs16LeaseContractEditWorkflowInitiatedFromList = createAction(
  '[IFRS16LeaseContract Edit: List] IFRS16 Lease Contract edit workflow initiated',
  props<{ editedInstance: IIFRS16LeaseContract }>()
);

export const ifrs16LeaseContractEditWorkflowInitiatedFromView = createAction(
  '[IFRS16LeaseContract Edit: View] IFRS16 Lease Contract edit workflow initiated',
  props<{ editedInstance: IIFRS16LeaseContract }>()
);

export const ifrs16LeaseContractCreationInitiatedEnRoute = createAction(
  '[IFRS16LeaseContract: Route] IFRS16 Lease Contract create workflow initiated',
);

export const ifrs16LeaseContractCreationWorkflowInitiatedFromList = createAction(
  '[IFRS16LeaseContract Create: List] IFRS16 Lease Contract create workflow initiated',
);

export const ifrs16LeaseContractUpdateFormHasBeenDestroyed = createAction(
  '[IFRS16LeaseContract Form] IFRS16 Lease Contract form destroyed',
);

export const ifrs16LeaseContractDataHasMutated = createAction(
  '[IFRS16LeaseContract Form] IFRS16 Lease Contract form data mutated',
);

export const ifrs16LeaseContractUpdateInstanceAcquiredFromBackend = createAction(
  '[IFRS16LeaseContractEffects: Copied-Lease-contract-effects] lease-contract-update instance acquired for copy',
  props<{backendAcquiredInstance: IIFRS16LeaseContract}>()
);

export const ifrs16LeaseContractUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[IFRS16LeaseContractEffects: Copied-Prepayment-Marshalling-effects] lease-contract-update instance acquisition failed',
  props<{error: string}>()
);
