import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportStatusComponent } from './list/report-status.component';
import { ReportStatusDetailComponent } from './detail/report-status-detail.component';
import { ReportStatusUpdateComponent } from './update/report-status-update.component';
import { ReportStatusDeleteDialogComponent } from './delete/report-status-delete-dialog.component';
import { ReportStatusRoutingModule } from './route/report-status-routing.module';

@NgModule({
  imports: [SharedModule, ReportStatusRoutingModule],
  declarations: [ReportStatusComponent, ReportStatusDetailComponent, ReportStatusUpdateComponent, ReportStatusDeleteDialogComponent],
  entryComponents: [ReportStatusDeleteDialogComponent],
})
export class ReportStatusModule {}
