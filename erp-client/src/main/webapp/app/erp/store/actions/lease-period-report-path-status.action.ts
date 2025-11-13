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

import { createAction, props } from '@ngrx/store';

export const leasePeriodReportPathUpdated = createAction(
  '[Generic lease period report path] Report view initiated with lease period report path',
  props<{ leasePeriodReportPathUpdate: string }>()
);

export const leasePeriodParamForRouAccountBalanceReportItem = createAction(
  '[ReportNavComponent] Report view initiated on the path rou-account-balance-report-item',
);

export const leasePeriodParamForRouAssetNBVReportItem = createAction(
  '[ReportNavComponent] Report view initiated on the path rou-asset-nbv-report-item',
);

export const leasePeriodParamForLeaseLiabilityReportItem = createAction(
  '[ReportNavComponent] Report view initiated on the path lease-liability-report-item',
);

export const leasePeriodParamForRouDepreciationPostingReportItem = createAction(
  '[ReportNavComponent] Report view initiated on the path rou-depreciation-posting-report-item',
);

export const leasePeriodReportPathReset = createAction(
  '[Generic lease period report path] Report path reset triggered',
);
