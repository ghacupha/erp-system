import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkInProgressOutstandingReportRequisitionComponent } from './list/work-in-progress-outstanding-report-requisition.component';
import { WorkInProgressOutstandingReportRequisitionDetailComponent } from './detail/work-in-progress-outstanding-report-requisition-detail.component';
import { WorkInProgressOutstandingReportRequisitionUpdateComponent } from './update/work-in-progress-outstanding-report-requisition-update.component';
import { WorkInProgressOutstandingReportRequisitionDeleteDialogComponent } from './delete/work-in-progress-outstanding-report-requisition-delete-dialog.component';
import { WorkInProgressOutstandingReportRequisitionRoutingModule } from './route/work-in-progress-outstanding-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, WorkInProgressOutstandingReportRequisitionRoutingModule],
  declarations: [
    WorkInProgressOutstandingReportRequisitionComponent,
    WorkInProgressOutstandingReportRequisitionDetailComponent,
    WorkInProgressOutstandingReportRequisitionUpdateComponent,
    WorkInProgressOutstandingReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [WorkInProgressOutstandingReportRequisitionDeleteDialogComponent],
})
export class WorkInProgressOutstandingReportRequisitionModule {}
