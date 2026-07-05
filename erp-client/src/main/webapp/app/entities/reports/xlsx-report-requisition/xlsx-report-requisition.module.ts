import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { XlsxReportRequisitionComponent } from './list/xlsx-report-requisition.component';
import { XlsxReportRequisitionDetailComponent } from './detail/xlsx-report-requisition-detail.component';
import { XlsxReportRequisitionUpdateComponent } from './update/xlsx-report-requisition-update.component';
import { XlsxReportRequisitionDeleteDialogComponent } from './delete/xlsx-report-requisition-delete-dialog.component';
import { XlsxReportRequisitionRoutingModule } from './route/xlsx-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, XlsxReportRequisitionRoutingModule],
  declarations: [
    XlsxReportRequisitionComponent,
    XlsxReportRequisitionDetailComponent,
    XlsxReportRequisitionUpdateComponent,
    XlsxReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [XlsxReportRequisitionDeleteDialogComponent],
})
export class XlsxReportRequisitionModule {}
