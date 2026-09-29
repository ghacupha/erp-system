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
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';
import {
  settlementInlineCreateCancelled,
  settlementInlineCreateCompleted,
  settlementInlineCreateConsumed,
  settlementInlineCreateStarted,
} from '../actions/settlement-inline-create.actions';

export const settlementInlineCreateFeatureKey = 'settlementInlineCreateState';

export interface SettlementInlineCreateState {
  active: boolean;
  targetField: string;
  parentRoute: string;
  parentFormSnapshot: unknown;
  createdSettlement: ISettlement | null;
}

export const initialSettlementInlineCreateState: SettlementInlineCreateState = {
  active: false,
  targetField: '',
  parentRoute: '',
  parentFormSnapshot: null,
  createdSettlement: null,
};

const _settlementInlineCreateReducer = createReducer(
  initialSettlementInlineCreateState,

  on(settlementInlineCreateStarted, (state, { targetField, parentRoute, parentFormSnapshot }) => ({
    ...state,
    active: true,
    targetField,
    parentRoute,
    parentFormSnapshot,
    createdSettlement: null,
  })),

  on(settlementInlineCreateCompleted, (state, { createdSettlement }) => ({
    ...state,
    createdSettlement,
  })),

  on(settlementInlineCreateConsumed, () => initialSettlementInlineCreateState),

  on(settlementInlineCreateCancelled, () => initialSettlementInlineCreateState)
);

export function settlementInlineCreateReducer(
  state: SettlementInlineCreateState = initialSettlementInlineCreateState,
  action: Action
): SettlementInlineCreateState {
  return _settlementInlineCreateReducer(state, action);
}
