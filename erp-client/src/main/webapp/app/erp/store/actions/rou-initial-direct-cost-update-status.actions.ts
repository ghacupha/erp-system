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
import { IRouInitialDirectCost } from '../../erp-leases/rou-initial-direct-cost/rou-initial-direct-cost.model';

export const rouInitialDirectCostCreationInitiatedFromList = createAction(
  '[RouInitialDirectCost Creation: List] ROU Initial Direct Cost creation workflow initiated',
);

export const rouInitialDirectCostCopyWorkflowInitiatedEnRoute = createAction(
  '[RouInitialDirectCost Copy: Route] ROU Initial Direct Cost copy workflow initiated',
  props<{ copiedInstance: IRouInitialDirectCost }>()
);

export const rouInitialDirectCostCopyWorkflowInitiatedFromList = createAction(
  '[RouInitialDirectCost Copy: List] ROU Initial Direct Cost copy workflow initiated',
  props<{ copiedInstance: IRouInitialDirectCost }>()
);

export const rouInitialDirectCostCopyWorkflowInitiatedFromView = createAction(
  '[RouInitialDirectCost Copy: View] ROU Initial Direct Cost copy workflow initiated',
  props<{ copiedInstance: IRouInitialDirectCost }>()
);

export const rouInitialDirectCostEditWorkflowInitiatedEnRoute = createAction(
  '[RouInitialDirectCost Edit: Route] ROU Initial Direct Cost edit workflow initiated',
  props<{ editedInstance: IRouInitialDirectCost }>()
);

export const rouInitialDirectCostEditWorkflowInitiatedFromList = createAction(
  '[RouInitialDirectCost Edit: List] ROU Initial Direct Cost edit workflow initiated',
  props<{ editedInstance: IRouInitialDirectCost }>()
);

export const rouInitialDirectCostEditWorkflowInitiatedFromView = createAction(
  '[RouInitialDirectCost Edit: View] ROU Initial Direct Cost edit workflow initiated',
  props<{ editedInstance: IRouInitialDirectCost }>()
);

export const rouInitialDirectCostCreationInitiatedEnRoute = createAction(
  '[RouInitialDirectCost: Route] ROU Initial Direct Cost create workflow initiated',
);

export const rouInitialDirectCostCreationWorkflowInitiatedFromList = createAction(
  '[RouInitialDirectCost Create: List] ROU Initial Direct Cost create workflow initiated',
);

export const rouInitialDirectCostUpdateFormHasBeenDestroyed = createAction(
  '[RouInitialDirectCost Form] ROU Initial Direct Cost form destroyed',
);

export const rouInitialDirectCostDataHasMutated = createAction(
  '[RouInitialDirectCost Form] ROU Initial Direct Cost form data mutated',
);

export const rouInitialDirectCostUpdateInstanceAcquiredFromBackend = createAction(
  '[RouInitialDirectCost Effects: ROU-Initial-Direct-Cost-effects] rou-initial-direct-cost update instance acquired for copy',
  props<{backendAcquiredInstance: IRouInitialDirectCost}>()
);

export const rouInitialDirectCostUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[RouInitialDirectCost Effects: ROU-Initial-Direct-Cost-effects] rou-initial-direct-cost update instance acquisition failed',
  props<{error: string}>()
);
