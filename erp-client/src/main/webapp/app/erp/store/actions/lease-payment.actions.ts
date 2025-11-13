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
import { ILeasePayment } from '../../erp-leases/lease-payment/lease-payment.model';

export const leasePaymentCreationInitiatedFromList = createAction(
  '[LeasePayment Creation: List] Lease Payment creation workflow initiated',
);

export const leasePaymentCopyWorkflowInitiatedEnRoute = createAction(
  '[LeasePayment Copy: Route] Lease Payment copy workflow initiated',
  props<{ copiedInstance: ILeasePayment }>()
);

export const leasePaymentCopyWorkflowInitiatedFromList = createAction(
  '[LeasePayment Copy: List] Lease Payment copy workflow initiated',
  props<{ copiedInstance: ILeasePayment }>()
);

export const leasePaymentCopyWorkflowInitiatedFromView = createAction(
  '[LeasePayment Copy: View] Lease Payment copy workflow initiated',
  props<{ copiedInstance: ILeasePayment }>()
);

export const leasePaymentEditWorkflowInitiatedEnRoute = createAction(
  '[LeasePayment Edit: Route] Lease Payment edit workflow initiated',
  props<{ editedInstance: ILeasePayment }>()
);

export const leasePaymentEditWorkflowInitiatedFromList = createAction(
  '[LeasePayment Edit: List] Lease Payment edit workflow initiated',
  props<{ editedInstance: ILeasePayment }>()
);

export const leasePaymentEditWorkflowInitiatedFromView = createAction(
  '[LeasePayment Edit: View] Lease Payment edit workflow initiated',
  props<{ editedInstance: ILeasePayment }>()
);

export const leasePaymentCreationInitiatedEnRoute = createAction(
  '[LeasePayment: Route] Lease Payment create workflow initiated',
);

export const leasePaymentCreationWorkflowInitiatedFromList = createAction(
  '[LeasePayment Create: List] Lease Payment create workflow initiated',
);

