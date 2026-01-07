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
import { ILeaseTemplate } from '../../erp-leases/lease-template/lease-template.model';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export const leaseTemplateCreationInitiatedFromList = createAction(
  '[LeaseTemplate Creation: List] Lease Template creation workflow initiated',
);

export const leaseTemplateCopyWorkflowInitiatedEnRoute = createAction(
  '[LeaseTemplate Copy: Route] Lease Template copy workflow initiated',
  props<{ copiedInstance: ILeaseTemplate }>()
);

export const leaseTemplateCopyWorkflowInitiatedFromList = createAction(
  '[LeaseTemplate Copy: List] Lease Template copy workflow initiated',
  props<{ copiedInstance: ILeaseTemplate }>()
);

export const leaseTemplateCopyWorkflowInitiatedFromView = createAction(
  '[LeaseTemplate Copy: View] Lease Template copy workflow initiated',
  props<{ copiedInstance: ILeaseTemplate }>()
);

export const leaseTemplateEditWorkflowInitiatedEnRoute = createAction(
  '[LeaseTemplate Edit: Route] Lease Template edit workflow initiated',
  props<{ editedInstance: ILeaseTemplate }>()
);

export const leaseTemplateEditWorkflowInitiatedFromList = createAction(
  '[LeaseTemplate Edit: List] Lease Template edit workflow initiated',
  props<{ editedInstance: ILeaseTemplate }>()
);

export const leaseTemplateEditWorkflowInitiatedFromView = createAction(
  '[LeaseTemplate Edit: View] Lease Template edit workflow initiated',
  props<{ editedInstance: ILeaseTemplate }>()
);

export const leaseTemplateCreationInitiatedEnRoute = createAction(
  '[LeaseTemplate: Route] Lease Template create workflow initiated',
);

export const leaseTemplateCreationWorkflowInitiatedFromList = createAction(
  '[LeaseTemplate Create: List] Lease Template create workflow initiated',
);

export const leaseTemplateCreationFromLeaseContractInitiatedFromList = createAction(
  '[LeaseTemplate Create: LeaseContract List] Lease Template create workflow initiated from lease contract',
  props<{ sourceLeaseContract: IIFRS16LeaseContract }>()
);

export const leaseTemplateCreationFromLeaseContractInitiatedFromView = createAction(
  '[LeaseTemplate Create: LeaseContract View] Lease Template create workflow initiated from lease contract',
  props<{ sourceLeaseContract: IIFRS16LeaseContract }>()
);

export const leaseTemplateUpdateFormHasBeenDestroyed = createAction(
  '[LeaseTemplate Form] Lease Template form destroyed',
);

export const leaseTemplateDataHasMutated = createAction(
  '[LeaseTemplate Form] Lease Template form data mutated',
);

export const leaseTemplateUpdateInstanceAcquiredFromBackend = createAction(
  '[LeaseTemplate Effects: LeaseTemplate-effects] lease-template update instance acquired for copy',
  props<{ backendAcquiredInstance: ILeaseTemplate }>()
);

export const leaseTemplateUpdateInstanceAcquisitionFromBackendFailed = createAction(
  '[LeaseTemplate Effects: LeaseTemplate-effects] lease-template update instance acquisition failed',
  props<{ error: string }>()
);

export const leaseTemplatePrefillDataLoaded = createAction(
  '[LeaseTemplate Effects: LeaseTemplate-effects] lease-template prefill data loaded',
  props<{ prefillTemplate: Partial<ILeaseTemplate>; sourceLeaseContract: IIFRS16LeaseContract }>()
);

export const leaseTemplatePrefillDataLoadFailed = createAction(
  '[LeaseTemplate Effects: LeaseTemplate-effects] lease-template prefill data load failed',
  props<{ error: string }>()
);
