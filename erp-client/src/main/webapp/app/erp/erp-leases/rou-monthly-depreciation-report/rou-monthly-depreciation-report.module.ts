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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouMonthlyDepreciationReportComponent } from './list/rou-monthly-depreciation-report.component';
import { RouMonthlyDepreciationReportDetailComponent } from './detail/rou-monthly-depreciation-report-detail.component';
import { RouMonthlyDepreciationReportUpdateComponent } from './update/rou-monthly-depreciation-report-update.component';
import { RouMonthlyDepreciationReportDeleteDialogComponent } from './delete/rou-monthly-depreciation-report-delete-dialog.component';
import { RouMonthlyDepreciationReportRoutingModule } from './route/rou-monthly-depreciation-report-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';

@NgModule({
  imports: [SharedModule, RouMonthlyDepreciationReportRoutingModule, ErpCommonModule],
  declarations: [
    RouMonthlyDepreciationReportComponent,
    RouMonthlyDepreciationReportDetailComponent,
    RouMonthlyDepreciationReportUpdateComponent,
    RouMonthlyDepreciationReportDeleteDialogComponent,
  ],
  entryComponents: [RouMonthlyDepreciationReportDeleteDialogComponent],
})
export class RouMonthlyDepreciationReportModule {}
