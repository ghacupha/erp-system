import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentReportRequisitionComponent } from './list/prepayment-report-requisition.component';
import { PrepaymentReportRequisitionDetailComponent } from './detail/prepayment-report-requisition-detail.component';
import { PrepaymentReportRequisitionUpdateComponent } from './update/prepayment-report-requisition-update.component';
import { PrepaymentReportRequisitionDeleteDialogComponent } from './delete/prepayment-report-requisition-delete-dialog.component';
import { PrepaymentReportRequisitionRoutingModule } from './route/prepayment-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentReportRequisitionRoutingModule],
  declarations: [
    PrepaymentReportRequisitionComponent,
    PrepaymentReportRequisitionDetailComponent,
    PrepaymentReportRequisitionUpdateComponent,
    PrepaymentReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentReportRequisitionDeleteDialogComponent],
})
export class PrepaymentReportRequisitionModule {}
