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

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import { leasePeriodHasBeenReset, leasePeriodSelected } from '../actions/lease-id-report-view-update.action';

export const leasePeriodIdSelectionUpdateFormStateSelector = 'leasePeriodIdSelection';

export interface LeasePeriodSelectionFormState {
  selectedLeasePeriodId: number| undefined;
  weAreCopying: boolean;
  weAreEditing: boolean;
  weAreCreating: boolean;
}

const _leasePeriodIdSelectionStateReducer = createReducer(
  initialState,

  // workflows for creation
  on(leasePeriodSelected, (state, {selectedLeasePeriodId}) => ({
    ...state,
    leasePeriodSelectionIdFormState: {
      ...state.leasePeriodSelectionIdFormState,
      selectedLeasePeriodId,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

  // workflows for creation
  on(leasePeriodHasBeenReset, (state) => ({
    ...state,
    leasePeriodSelectionIdFormState: {
      ...state.leasePeriodSelectionIdFormState,
      selectedLeasePeriodId: undefined,
      weAreCopying: false,
      weAreEditing: false,
      weAreCreating: true,
    }
  })),

);

export function leasePeriodIdSelectionStateReducer(state: State = initialState, action: Action): State {

  return _leasePeriodIdSelectionStateReducer(state, action);
}
