import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbReportRequestReasonsComponent } from './list/crb-report-request-reasons.component';
import { CrbReportRequestReasonsDetailComponent } from './detail/crb-report-request-reasons-detail.component';
import { CrbReportRequestReasonsUpdateComponent } from './update/crb-report-request-reasons-update.component';
import { CrbReportRequestReasonsDeleteDialogComponent } from './delete/crb-report-request-reasons-delete-dialog.component';
import { CrbReportRequestReasonsRoutingModule } from './route/crb-report-request-reasons-routing.module';

@NgModule({
  imports: [SharedModule, CrbReportRequestReasonsRoutingModule],
  declarations: [
    CrbReportRequestReasonsComponent,
    CrbReportRequestReasonsDetailComponent,
    CrbReportRequestReasonsUpdateComponent,
    CrbReportRequestReasonsDeleteDialogComponent,
  ],
  entryComponents: [CrbReportRequestReasonsDeleteDialogComponent],
})
export class CrbReportRequestReasonsModule {}
