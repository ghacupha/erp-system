import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PdfReportRequisitionComponent } from './list/pdf-report-requisition.component';
import { PdfReportRequisitionDetailComponent } from './detail/pdf-report-requisition-detail.component';
import { PdfReportRequisitionUpdateComponent } from './update/pdf-report-requisition-update.component';
import { PdfReportRequisitionDeleteDialogComponent } from './delete/pdf-report-requisition-delete-dialog.component';
import { PdfReportRequisitionRoutingModule } from './route/pdf-report-requisition-routing.module';

@NgModule({
  imports: [SharedModule, PdfReportRequisitionRoutingModule],
  declarations: [
    PdfReportRequisitionComponent,
    PdfReportRequisitionDetailComponent,
    PdfReportRequisitionUpdateComponent,
    PdfReportRequisitionDeleteDialogComponent,
  ],
  entryComponents: [PdfReportRequisitionDeleteDialogComponent],
})
export class PdfReportRequisitionModule {}
