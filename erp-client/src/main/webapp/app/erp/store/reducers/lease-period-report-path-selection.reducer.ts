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
  leasePeriodParamForLeaseLiabilityReportItem,
  leasePeriodParamForRouAccountBalanceReportItem,
  leasePeriodParamForRouAssetNBVReportItem,
  leasePeriodParamForRouDepreciationPostingReportItem,
  leasePeriodReportPathReset,
  leasePeriodReportPathUpdated
} from '../actions/lease-period-report-path-status.action';

export const leasePeriodReportPathSelectionStateSelector = 'leasePeriodReportPath';

export interface LeasePeriodReportPathSelectionState {
  leasePeriodReportPath: string;
  leasePeriodReportTitle: string;
}

const _leasePeriodReportPathSelectionStateReducer = createReducer(
  initialState,

  on(leasePeriodReportPathUpdated, (state, { leasePeriodReportPathUpdate }) => ({
    ...state,
    leasePeriodReportPathSelectionState: {
      ...state.leasePeriodReportPathSelectionState,
      leasePeriodReportPath: leasePeriodReportPathUpdate,
    }
  })),

  on(leasePeriodParamForRouAccountBalanceReportItem, (state) => ({
    ...state,
    leasePeriodReportPathSelectionState: {
      ...state.leasePeriodReportPathSelectionState,
      leasePeriodReportPath: 'rou-account-balance-report-item',
      leasePeriodReportTitle: 'Lease Period Parameter For Account Balance Report',
    }
  })),

  on(leasePeriodParamForRouAssetNBVReportItem, (state) => ({
    ...state,
    leasePeriodReportPathSelectionState: {
      ...state.leasePeriodReportPathSelectionState,
      leasePeriodReportPath: 'rou-asset-nbv-report-item',
      leasePeriodReportTitle: 'Lease Period Parameter For Asset NBV Report',
    }
  })),

  on(leasePeriodParamForLeaseLiabilityReportItem, (state) => ({
    ...state,
    leasePeriodReportPathSelectionState: {
      ...state.leasePeriodReportPathSelectionState,
      leasePeriodReportPath: 'lease-liability-report-item',
      leasePeriodReportTitle: 'Lease Period Parameter For Lease Liability Report',
    }
  })),

  on(leasePeriodParamForRouDepreciationPostingReportItem, (state) => ({
    ...state,
    leasePeriodReportPathSelectionState: {
      ...state.leasePeriodReportPathSelectionState,
      leasePeriodReportPath: 'rou-depreciation-posting-report-item',
      leasePeriodReportTitle: 'Lease Period Parameter For Depreciation Posting Report',
    }
  })),

  // workflows for creation
  on(leasePeriodReportPathReset, (state) => ({
    ...state,
    leasePeriodReportPathSelectionState: {
      ...state.leasePeriodReportPathSelectionState,
      leasePeriodReportPath: '',
    }
  }))
);

export function leasePeriodReportPathSelectionStateReducer(state: State = initialState, action: Action): State {

  return _leasePeriodReportPathSelectionStateReducer(state, action);
}
