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

import { Action, createReducer, on } from '@ngrx/store';
import {
  inlineCreateCancelled,
  inlineCreateCompleted,
  inlineCreateConsumed,
  inlineCreateRequested,
} from '../actions/inline-create-return.actions';

export const inlineCreateReturnFeatureKey = 'inlineCreateReturnState';

export interface PendingInlineCreate {
  correlationId: string;
  entityType: string;
  targetField: string;
  returnUrl: string;
}

export interface CompletedInlineCreate {
  correlationId: string;
  entityType: string;
  targetField: string;
  createdEntity: unknown;
}

export interface InlineCreateReturnState {
  // Keyed by correlationId so several inline-create requests (e.g. a nested
  // create-Invoice-while-creating-Settlement) can be in flight at once.
  pending: { [correlationId: string]: PendingInlineCreate };
  completed: { [correlationId: string]: CompletedInlineCreate };
}

export const initialInlineCreateReturnState: InlineCreateReturnState = {
  pending: {},
  completed: {},
};

const _inlineCreateReturnReducer = createReducer(
  initialInlineCreateReturnState,

  on(inlineCreateRequested, (state, { correlationId, entityType, targetField, returnUrl }) => ({
    ...state,
    pending: {
      ...state.pending,
      [correlationId]: { correlationId, entityType, targetField, returnUrl },
    },
  })),

  on(inlineCreateCompleted, (state, { correlationId, entityType, targetField, createdEntity }) => ({
    ...state,
    completed: {
      ...state.completed,
      [correlationId]: { correlationId, entityType, targetField, createdEntity },
    },
  })),

  on(inlineCreateConsumed, (state, { correlationId }) => {
    const pending = { ...state.pending };
    const completed = { ...state.completed };
    delete pending[correlationId];
    delete completed[correlationId];
    return { ...state, pending, completed };
  }),

  on(inlineCreateCancelled, (state, { correlationId }) => {
    const pending = { ...state.pending };
    delete pending[correlationId];
    return { ...state, pending };
  })
);

export function inlineCreateReturnReducer(state: InlineCreateReturnState = initialInlineCreateReturnState, action: Action): InlineCreateReturnState {
  return _inlineCreateReturnReducer(state, action);
}
