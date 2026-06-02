import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WIPTransferListReportComponent } from './list/wip-transfer-list-report.component';
import { WIPTransferListReportDetailComponent } from './detail/wip-transfer-list-report-detail.component';
import { WIPTransferListReportUpdateComponent } from './update/wip-transfer-list-report-update.component';
import { WIPTransferListReportDeleteDialogComponent } from './delete/wip-transfer-list-report-delete-dialog.component';
import { WIPTransferListReportRoutingModule } from './route/wip-transfer-list-report-routing.module';

@NgModule({
  imports: [SharedModule, WIPTransferListReportRoutingModule],
  declarations: [
    WIPTransferListReportComponent,
    WIPTransferListReportDetailComponent,
    WIPTransferListReportUpdateComponent,
    WIPTransferListReportDeleteDialogComponent,
  ],
  entryComponents: [WIPTransferListReportDeleteDialogComponent],
})
export class WIPTransferListReportModule {}
