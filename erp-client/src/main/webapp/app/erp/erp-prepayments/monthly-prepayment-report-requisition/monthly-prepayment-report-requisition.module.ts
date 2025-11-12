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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MonthlyPrepaymentReportRequisitionComponent } from './list/monthly-prepayment-report-requisition.component';
import { MonthlyPrepaymentReportRequisitionDetailComponent } from './detail/monthly-prepayment-report-requisition-detail.component';
import { MonthlyPrepaymentReportRequisitionUpdateComponent } from './update/monthly-prepayment-report-requisition-update.component';
import { MonthlyPrepaymentReportRequisitionDeleteDialogComponent } from './delete/monthly-prepayment-report-requisition-delete-dialog.component';
import { MonthlyPrepaymentReportRequisitionRoutingModule } from './route/monthly-prepayment-report-requisition-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';
import { MonthlyPrepaymentReportRequisitionExportComponent } from './export/monthly-prepayment-report-requisition-export.component';

@NgModule({
  imports: [
    SharedModule,
    MonthlyPrepaymentReportRequisitionRoutingModule,
    ErpCommonModule],
  declarations: [
    MonthlyPrepaymentReportRequisitionComponent,
    MonthlyPrepaymentReportRequisitionDetailComponent,
    MonthlyPrepaymentReportRequisitionUpdateComponent,
    MonthlyPrepaymentReportRequisitionExportComponent,
    MonthlyPrepaymentReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [MonthlyPrepaymentReportRequisitionDeleteDialogComponent],
})
export class MonthlyPrepaymentReportRequisitionModule {}
