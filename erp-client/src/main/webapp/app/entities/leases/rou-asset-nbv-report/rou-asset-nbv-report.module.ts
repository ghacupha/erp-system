import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouAssetNBVReportComponent } from './list/rou-asset-nbv-report.component';
import { RouAssetNBVReportDetailComponent } from './detail/rou-asset-nbv-report-detail.component';
import { RouAssetNBVReportUpdateComponent } from './update/rou-asset-nbv-report-update.component';
import { RouAssetNBVReportDeleteDialogComponent } from './delete/rou-asset-nbv-report-delete-dialog.component';
import { RouAssetNBVReportRoutingModule } from './route/rou-asset-nbv-report-routing.module';

@NgModule({
  imports: [SharedModule, RouAssetNBVReportRoutingModule],
  declarations: [
    RouAssetNBVReportComponent,
    RouAssetNBVReportDetailComponent,
    RouAssetNBVReportUpdateComponent,
    RouAssetNBVReportDeleteDialogComponent,
  ],
  entryComponents: [RouAssetNBVReportDeleteDialogComponent],
})
export class RouAssetNBVReportModule {}
