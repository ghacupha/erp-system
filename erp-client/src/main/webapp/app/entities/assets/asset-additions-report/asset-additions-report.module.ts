import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AssetAdditionsReportComponent } from './list/asset-additions-report.component';
import { AssetAdditionsReportDetailComponent } from './detail/asset-additions-report-detail.component';
import { AssetAdditionsReportUpdateComponent } from './update/asset-additions-report-update.component';
import { AssetAdditionsReportDeleteDialogComponent } from './delete/asset-additions-report-delete-dialog.component';
import { AssetAdditionsReportRoutingModule } from './route/asset-additions-report-routing.module';

@NgModule({
  imports: [SharedModule, AssetAdditionsReportRoutingModule],
  declarations: [
    AssetAdditionsReportComponent,
    AssetAdditionsReportDetailComponent,
    AssetAdditionsReportUpdateComponent,
    AssetAdditionsReportDeleteDialogComponent,
  ],
  entryComponents: [AssetAdditionsReportDeleteDialogComponent],
})
export class AssetAdditionsReportModule {}
