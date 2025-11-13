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
import { taLeaseRepaymentRuleUpdateFormStateSelector } from '../reducers/ta-lease-repayment-rule-status.reducer';

export const taLeaseRepaymentRuleFormState = createFeatureSelector<State>(taLeaseRepaymentRuleUpdateFormStateSelector);

export const taLeaseRepaymentRuleSelectedInstance = createSelector(
  taLeaseRepaymentRuleFormState,
  state => state.taLeaseRepaymentRuleFormState.selectedInstance
);

export const editingTALeaseRepaymentRuleStatus = createSelector(
  taLeaseRepaymentRuleFormState,
  state => state.taLeaseRepaymentRuleFormState.weAreEditing
);

export const creatingTALeaseRepaymentRuleStatus = createSelector(
  taLeaseRepaymentRuleFormState,
  state => state.taLeaseRepaymentRuleFormState.weAreCreating
);

export const copyingTALeaseRepaymentRuleStatus = createSelector(
  taLeaseRepaymentRuleFormState,
  state => state.taLeaseRepaymentRuleFormState.weAreCopying
);
