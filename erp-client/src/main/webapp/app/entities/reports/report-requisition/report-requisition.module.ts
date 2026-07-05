import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportRequisitionComponent } from './list/report-requisition.component';
import { ReportRequisitionDetailComponent } from './detail/report-requisition-detail.component';
import { ReportRequisitionUpdateComponent } from './update/report-requisition-update.component';
import { ReportRequisitionDeleteDialogComponent } from './delete/report-requisition-delete-dialog.component';
import { ReportRequisitionRoutingModule } from './route/report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, ReportRequisitionRoutingModule],
  declarations: [
    ReportRequisitionComponent,
    ReportRequisitionDetailComponent,
    ReportRequisitionUpdateComponent,
    ReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [ReportRequisitionDeleteDialogComponent],
})
export class ReportRequisitionModule {}
