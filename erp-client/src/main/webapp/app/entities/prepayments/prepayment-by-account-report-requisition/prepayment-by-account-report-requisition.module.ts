import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentByAccountReportRequisitionComponent } from './list/prepayment-by-account-report-requisition.component';
import { PrepaymentByAccountReportRequisitionDetailComponent } from './detail/prepayment-by-account-report-requisition-detail.component';
import { PrepaymentByAccountReportRequisitionUpdateComponent } from './update/prepayment-by-account-report-requisition-update.component';
import { PrepaymentByAccountReportRequisitionDeleteDialogComponent } from './delete/prepayment-by-account-report-requisition-delete-dialog.component';
import { PrepaymentByAccountReportRequisitionRoutingModule } from './route/prepayment-by-account-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentByAccountReportRequisitionRoutingModule],
  declarations: [
    PrepaymentByAccountReportRequisitionComponent,
    PrepaymentByAccountReportRequisitionDetailComponent,
    PrepaymentByAccountReportRequisitionUpdateComponent,
    PrepaymentByAccountReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [PrepaymentByAccountReportRequisitionDeleteDialogComponent],
})
export class PrepaymentByAccountReportRequisitionModule {}
