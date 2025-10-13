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
import { transactionAccountUpdateFormStateSelector } from '../reducers/transaction-account-update-status.reducer';

export const transactionAccountFormState = createFeatureSelector<State>(transactionAccountUpdateFormStateSelector);

export const transactionAccountUpdateSelectedInstance = createSelector(
  transactionAccountFormState,
  state => state.transactionAccountFormState.selectedInstance
);

export const editingTransactionAccountStatus = createSelector(
  transactionAccountFormState,
  state => state.transactionAccountFormState.weAreEditing
);

export const creatingTransactionAccountStatus = createSelector(
  transactionAccountFormState,
  state => state.transactionAccountFormState.weAreCreating
);

export const copyingTransactionAccountStatus = createSelector(
  transactionAccountFormState,
  state => state.transactionAccountFormState.weAreCopying
);
