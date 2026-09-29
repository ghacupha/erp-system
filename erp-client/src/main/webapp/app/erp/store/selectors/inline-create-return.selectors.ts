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

import { createFeatureSelector, createSelector, MemoizedSelector } from '@ngrx/store';
import { State } from '../global-store.definition';
import { inlineCreateReturnFeatureKey, CompletedInlineCreate, PendingInlineCreate } from '../reducers/inline-create-return.reducer';

export const inlineCreateReturnFeatureSelector = createFeatureSelector<State>(inlineCreateReturnFeatureKey);

export const selectPendingInlineCreate = (correlationId: string): MemoizedSelector<State, PendingInlineCreate> =>
  createSelector(inlineCreateReturnFeatureSelector, state => state.inlineCreateReturnState.pending[correlationId]);

export const selectCompletedInlineCreate = (correlationId: string): MemoizedSelector<State, CompletedInlineCreate> =>
  createSelector(inlineCreateReturnFeatureSelector, state => state.inlineCreateReturnState.completed[correlationId]);

// Used by an originating form (e.g. prepayment-account, or settlement while it hosts the
// invoice picker) to find any completion meant for one of its own fields, without needing
// to already know the correlationId (a full page reload loses component-local state, but
// this NgRx slice and the correlationId carried in the return URL's query params survive it).
export const selectCompletedInlineCreatesForField = (targetField: string): MemoizedSelector<State, CompletedInlineCreate[]> =>
  createSelector(inlineCreateReturnFeatureSelector, state =>
    Object.values(state.inlineCreateReturnState.completed).filter(completed => completed.targetField === targetField)
  );
