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

import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from '../global-store.definition';
import { settlementUpdateFormStateSelector } from '../reducers/settlement-update-menu-status.reducer';

export const settlementStatusFeatureSelector = createFeatureSelector<State>(settlementUpdateFormStateSelector);

export const settlementUpdateSelectedPayment = createSelector(
  settlementStatusFeatureSelector,
  state => state.settlementsFormState.selectedSettlement
);

export const settlementBackEndFetchCompletion = createSelector(
  settlementStatusFeatureSelector,
  state => state.settlementsFormState.backEndFetchComplete
);

export const editingSettlementStatus = createSelector(
  settlementStatusFeatureSelector,
  state => state.settlementsFormState.weAreEditing
);

export const creatingSettlementStatus = createSelector(
  settlementStatusFeatureSelector,
  state => state.settlementsFormState.weAreCreating
);

export const copyingSettlementStatus = createSelector(
  settlementStatusFeatureSelector,
  state => state.settlementsFormState.weAreCopying
);

export const settlementBrowserRefreshStatus = createSelector(
  settlementStatusFeatureSelector,
  state => state.settlementsFormState.browserHasBeenRefreshed
);
