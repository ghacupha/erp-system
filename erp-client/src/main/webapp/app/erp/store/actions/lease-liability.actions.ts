///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { ILeaseLiability } from '../../erp-leases/lease-liability/lease-liability.model';

export const leaseLiabilityCreationInitiatedFromList = createAction(
  '[LeaseLiability Creation: List] Lease Liability creation workflow initiated',
);

export const leaseLiabilityCopyWorkflowInitiatedEnRoute = createAction(
  '[LeaseLiability Copy: Route] Lease Liability copy workflow initiated',
  props<{ copiedInstance: ILeaseLiability }>()
);

export const leaseLiabilityCopyWorkflowInitiatedFromList = createAction(
  '[LeaseLiability Copy: List] Lease Liability copy workflow initiated',
  props<{ copiedInstance: ILeaseLiability }>()
);

export const leaseLiabilityCopyWorkflowInitiatedFromView = createAction(
  '[LeaseLiability Copy: View] Lease Liability copy workflow initiated',
  props<{ copiedInstance: ILeaseLiability }>()
);

export const leaseLiabilityEditWorkflowInitiatedEnRoute = createAction(
  '[LeaseLiability Edit: Route] Lease Liability edit workflow initiated',
  props<{ editedInstance: ILeaseLiability }>()
);

export const leaseLiabilityEditWorkflowInitiatedFromList = createAction(
  '[LeaseLiability Edit: List] Lease Liability edit workflow initiated',
  props<{ editedInstance: ILeaseLiability }>()
);

export const leaseLiabilityEditWorkflowInitiatedFromView = createAction(
  '[LeaseLiability Edit: View] Lease Liability edit workflow initiated',
  props<{ editedInstance: ILeaseLiability }>()
);

export const leaseLiabilityCreationInitiatedEnRoute = createAction(
  '[LeaseLiability: Route] Lease Liability create workflow initiated',
);

export const leaseLiabilityCreationWorkflowInitiatedFromList = createAction(
  '[LeaseLiability Create: List] Lease Liability create workflow initiated',
);

