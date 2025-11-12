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

import { Action, createReducer, on } from '@ngrx/store';
import { initialState, State } from '../global-store.definition';
import {
  ifrs16LeaseContractReportReset,
  ifrs16LeaseContractReportSelected,
} from '../actions/ifrs16-lease-contract-report.actions';

export const ifrs16LeaseContractReportStateSelector = 'ifrs16LeaseContractReport';

export interface IFRS16LeaseContractReportState {
  selectedLeaseContractId: number | undefined;
}

const _ifrs16LeaseContractReportStateReducer = createReducer(
  initialState,
  on(ifrs16LeaseContractReportSelected, (state, { selectedLeaseContractId }) => ({
    ...state,
    ifrs16LeaseContractReportState: {
      ...state.ifrs16LeaseContractReportState,
      selectedLeaseContractId,
    },
  })),
  on(ifrs16LeaseContractReportReset, state => ({
    ...state,
    ifrs16LeaseContractReportState: {
      ...state.ifrs16LeaseContractReportState,
      selectedLeaseContractId: undefined,
    },
  }))
);

export function ifrs16LeaseContractReportStateReducer(state: State = initialState, action: Action): State {
  return _ifrs16LeaseContractReportStateReducer(state, action);
}
