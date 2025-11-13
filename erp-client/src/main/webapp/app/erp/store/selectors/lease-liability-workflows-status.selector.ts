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

import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from '../global-store.definition';
import { leaseLiabilityFormStateSelector } from '../reducers/lease-liability.reducer';

export const leaseLiabilityUpdateFormState = createFeatureSelector<State>(leaseLiabilityFormStateSelector);

export const leaseLiabilitySelectedInstance = createSelector(
  leaseLiabilityUpdateFormState,
  state => state.leaseLiabilityFormState.selectedInstance
);

export const editingLeaseLiabilityStatus = createSelector(
  leaseLiabilityUpdateFormState,
  state => state.leaseLiabilityFormState.weAreEditing
);

export const creatingLeaseLiabilityStatus = createSelector(
  leaseLiabilityUpdateFormState,
  state => state.leaseLiabilityFormState.weAreCreating
);

export const copyingLeaseLiabilityStatus = createSelector(
  leaseLiabilityUpdateFormState,
  state => state.leaseLiabilityFormState.weAreCopying
);
