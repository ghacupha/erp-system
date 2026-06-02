import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WIPListReportComponent } from './list/wip-list-report.component';
import { WIPListReportDetailComponent } from './detail/wip-list-report-detail.component';
import { WIPListReportUpdateComponent } from './update/wip-list-report-update.component';
import { WIPListReportDeleteDialogComponent } from './delete/wip-list-report-delete-dialog.component';
import { WIPListReportRoutingModule } from './route/wip-list-report-routing.module';

@NgModule({
  imports: [SharedModule, WIPListReportRoutingModule],
  declarations: [WIPListReportComponent, WIPListReportDetailComponent, WIPListReportUpdateComponent, WIPListReportDeleteDialogComponent],
  entryComponents: [WIPListReportDeleteDialogComponent],
})
export class WIPListReportModule {}
