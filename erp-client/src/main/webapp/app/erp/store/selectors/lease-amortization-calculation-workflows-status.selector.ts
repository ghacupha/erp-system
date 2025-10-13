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

import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from '../global-store.definition';
import { leaseAmortizationCalculationFormStateSelector } from '../reducers/lease-amortization-calculation.reducer';

export const leaseAmortizationCalculationUpdateFormState = createFeatureSelector<State>(leaseAmortizationCalculationFormStateSelector);

export const leaseAmortizationCalculationSelectedInstance = createSelector(
  leaseAmortizationCalculationUpdateFormState,
  state => state.leaseAmortizationCalculationFormState.selectedInstance
);

export const editingLeaseAmortizationCalculationStatus = createSelector(
  leaseAmortizationCalculationUpdateFormState,
  state => state.leaseAmortizationCalculationFormState.weAreEditing
);

export const creatingLeaseAmortizationCalculationStatus = createSelector(
  leaseAmortizationCalculationUpdateFormState,
  state => state.leaseAmortizationCalculationFormState.weAreCreating
);

export const copyingLeaseAmortizationCalculationStatus = createSelector(
  leaseAmortizationCalculationUpdateFormState,
  state => state.leaseAmortizationCalculationFormState.weAreCopying
);
