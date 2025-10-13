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

import { createAction, props } from '@ngrx/store';

export const wipOverviewReportNavigationInitiatedFromNavbar = createAction(
  '[WIP Overview Navigation: Navbar] Report navigation initiated',
  props<{ wipOverviewReportNavigationPath: string }>()
);

export const wipOverviewReportNavigationInitiatedFromReportDateModal = createAction(
  '[WIP Overview Navigation: Report Date Modal] Report navigation initiated',
  props<{ wipOverviewReportDate: string }>()
);

export const wipOverviewResetReportPathAction = createAction(
  '[WIP Overview Navigation] Reset report-path profile'
);

export const wipOverviewResetReportDateAction = createAction(
  '[WIP Overview Navigation] Reset report-date profile'
);

export const prepaymentOverviewReportNavigationInitiatedFromNavbar = createAction(
  '[Prepayment Overview Navigation: Navbar] Report navigation initiated',
  props<{ prepaymentOverviewReportNavigationPath: string }>()
);

export const prepaymentOverviewReportNavigationInitiatedFromReportDateModal = createAction(
  '[Prepayment Overview Navigation: Report Date Modal] Report navigation initiated',
  props<{ prepaymentOverviewReportDate: string }>()
);

export const prepaymentOverviewResetReportPathAction = createAction(
  '[Prepayment Overview Navigation] Reset report-path profile'
);

export const prepaymentOverviewResetReportDateAction = createAction(
  '[Prepayment Overview Navigation] Reset report-date profile'
);
