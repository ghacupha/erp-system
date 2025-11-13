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
import { initialState, State } from '../global-store.definition';
import {
  prepaymentOverviewReportNavigationInitiatedFromNavbar,
  prepaymentOverviewReportNavigationInitiatedFromReportDateModal, prepaymentOverviewResetReportDateAction,
  prepaymentOverviewResetReportPathAction,
  wipOverviewReportNavigationInitiatedFromNavbar,
  wipOverviewReportNavigationInitiatedFromReportDateModal,
  wipOverviewResetReportDateAction,
  wipOverviewResetReportPathAction
} from '../actions/report-navigation-profile-status.actions';
import dayjs from 'dayjs';
import { DATE_FORMAT } from '../../../config/input.constants';

export const reportNavigationProfileStateSelector = 'reportNavigationProfileState';

export interface ReportNavigationProfileState {
  reportPath: string,
  reportDate: string,
  reportTitle: string,
}

const _reportNavigationProfileStateReducer = createReducer(
  initialState,

  // workflows nav from navbar
  on(wipOverviewReportNavigationInitiatedFromNavbar, (state, {wipOverviewReportNavigationPath}) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportPath: wipOverviewReportNavigationPath,
      reportTitle: 'Work In Progress Overview'
    }
  })),

  //    workflows for nav from report-date modal
  on(wipOverviewReportNavigationInitiatedFromReportDateModal, (state, {wipOverviewReportDate}) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportDate: wipOverviewReportDate
    }
  })),

  on(wipOverviewResetReportPathAction, (state) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportPath: '',
      reportTitle: 'ERP Reports'
    }
  })),

  on(wipOverviewResetReportDateAction, (state) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportDate: dayjs().format(DATE_FORMAT)
    }
  })),


  // workflows nav from navbar
  on(prepaymentOverviewReportNavigationInitiatedFromNavbar, (state, {prepaymentOverviewReportNavigationPath}) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportPath: prepaymentOverviewReportNavigationPath,
      reportTitle: 'Prepayment Outstanding Overview'
    }
  })),

  //    workflows for nav from report-date modal
  on(prepaymentOverviewReportNavigationInitiatedFromReportDateModal, (state, {prepaymentOverviewReportDate}) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportDate: prepaymentOverviewReportDate
    }
  })),

  on(prepaymentOverviewResetReportPathAction, (state) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportPath: '',
      reportTitle: 'ERP Reports'
    }
  })),

  on(prepaymentOverviewResetReportDateAction, (state) => ({
    ...state,
    reportNavigationProfileState: {
      ...state.reportNavigationProfileState,
      reportDate: dayjs().format(DATE_FORMAT)
    }
  })),

);

export function reportNavigationProfileStateReducer(state: State = initialState, action: Action): State {

  return _reportNavigationProfileStateReducer(state, action);
}

