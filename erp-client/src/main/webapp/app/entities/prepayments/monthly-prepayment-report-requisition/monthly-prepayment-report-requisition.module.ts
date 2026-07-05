import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MonthlyPrepaymentReportRequisitionComponent } from './list/monthly-prepayment-report-requisition.component';
import { MonthlyPrepaymentReportRequisitionDetailComponent } from './detail/monthly-prepayment-report-requisition-detail.component';
import { MonthlyPrepaymentReportRequisitionUpdateComponent } from './update/monthly-prepayment-report-requisition-update.component';
import { MonthlyPrepaymentReportRequisitionDeleteDialogComponent } from './delete/monthly-prepayment-report-requisition-delete-dialog.component';
import { MonthlyPrepaymentReportRequisitionRoutingModule } from './route/monthly-prepayment-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, MonthlyPrepaymentReportRequisitionRoutingModule],
  declarations: [
    MonthlyPrepaymentReportRequisitionComponent,
    MonthlyPrepaymentReportRequisitionDetailComponent,
    MonthlyPrepaymentReportRequisitionUpdateComponent,
    MonthlyPrepaymentReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [MonthlyPrepaymentReportRequisitionDeleteDialogComponent],
})
export class MonthlyPrepaymentReportRequisitionModule {}
