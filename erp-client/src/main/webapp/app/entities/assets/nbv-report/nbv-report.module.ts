import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NbvReportComponent } from './list/nbv-report.component';
import { NbvReportDetailComponent } from './detail/nbv-report-detail.component';
import { NbvReportUpdateComponent } from './update/nbv-report-update.component';
import { NbvReportDeleteDialogComponent } from './delete/nbv-report-delete-dialog.component';
import { NbvReportRoutingModule } from './route/nbv-report-routing.module';

@NgModule({
  imports: [SharedModule, NbvReportRoutingModule],
  declarations: [NbvReportComponent, NbvReportDetailComponent, NbvReportUpdateComponent, NbvReportDeleteDialogComponent],
  entryComponents: [NbvReportDeleteDialogComponent],
})
export class NbvReportModule {}
