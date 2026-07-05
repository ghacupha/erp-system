import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouAssetNBVReportItemComponent } from './list/rou-asset-nbv-report-item.component';
import { RouAssetNBVReportItemDetailComponent } from './detail/rou-asset-nbv-report-item-detail.component';
import { RouAssetNBVReportItemRoutingModule } from './route/rou-asset-nbv-report-item-routing.module';

@NgModule({
  imports: [SharedModule, RouAssetNBVReportItemRoutingModule],
  declarations: [RouAssetNBVReportItemComponent, RouAssetNBVReportItemDetailComponent],
})
export class RouAssetNBVReportItemModule {}
