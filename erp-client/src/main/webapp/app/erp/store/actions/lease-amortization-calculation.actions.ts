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
import { ILeaseAmortizationCalculation } from '../../erp-leases/lease-amortization-calculation/lease-amortization-calculation.model';

export const leaseAmortizationCalculationCreationInitiatedFromList = createAction(
  '[LeaseAmortizationCalculation Creation: List] Lease Amortization Calculation creation workflow initiated',
);

export const leaseAmortizationCalculationCopyWorkflowInitiatedEnRoute = createAction(
  '[LeaseAmortizationCalculation Copy: Route] Lease Amortization Calculation copy workflow initiated',
  props<{ copiedInstance: ILeaseAmortizationCalculation }>()
);

export const leaseAmortizationCalculationCopyWorkflowInitiatedFromList = createAction(
  '[LeaseAmortizationCalculation Copy: List] Lease Amortization Calculation copy workflow initiated',
  props<{ copiedInstance: ILeaseAmortizationCalculation }>()
);

export const leaseAmortizationCalculationCopyWorkflowInitiatedFromView = createAction(
  '[LeaseAmortizationCalculation Copy: View] Lease Amortization Calculation copy workflow initiated',
  props<{ copiedInstance: ILeaseAmortizationCalculation }>()
);

export const leaseAmortizationCalculationEditWorkflowInitiatedEnRoute = createAction(
  '[LeaseAmortizationCalculation Edit: Route] Lease Amortization Calculation edit workflow initiated',
  props<{ editedInstance: ILeaseAmortizationCalculation }>()
);

export const leaseAmortizationCalculationEditWorkflowInitiatedFromList = createAction(
  '[LeaseAmortizationCalculation Edit: List] Lease Amortization Calculation edit workflow initiated',
  props<{ editedInstance: ILeaseAmortizationCalculation }>()
);

export const leaseAmortizationCalculationEditWorkflowInitiatedFromView = createAction(
  '[LeaseAmortizationCalculation Edit: View] Lease Amortization Calculation edit workflow initiated',
  props<{ editedInstance: ILeaseAmortizationCalculation }>()
);

export const leaseAmortizationCalculationCreationInitiatedEnRoute = createAction(
  '[LeaseAmortizationCalculation: Route] Lease Amortization Calculation create workflow initiated',
);

export const leaseAmortizationCalculationCreationWorkflowInitiatedFromList = createAction(
  '[LeaseAmortizationCalculation Create: List] Lease Amortization Calculation create workflow initiated',
);

