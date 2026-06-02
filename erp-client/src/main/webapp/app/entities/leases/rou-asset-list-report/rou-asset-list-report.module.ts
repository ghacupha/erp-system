import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouAssetListReportComponent } from './list/rou-asset-list-report.component';
import { RouAssetListReportDetailComponent } from './detail/rou-asset-list-report-detail.component';
import { RouAssetListReportUpdateComponent } from './update/rou-asset-list-report-update.component';
import { RouAssetListReportDeleteDialogComponent } from './delete/rou-asset-list-report-delete-dialog.component';
import { RouAssetListReportRoutingModule } from './route/rou-asset-list-report-routing.module';

@NgModule({
  imports: [SharedModule, RouAssetListReportRoutingModule],
  declarations: [
    RouAssetListReportComponent,
    RouAssetListReportDetailComponent,
    RouAssetListReportUpdateComponent,
    RouAssetListReportDeleteDialogComponent,
  ],
  entryComponents: [RouAssetListReportDeleteDialogComponent],
})
export class RouAssetListReportModule {}
