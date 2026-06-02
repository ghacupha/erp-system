import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouAssetListReportItemComponent } from './list/rou-asset-list-report-item.component';
import { RouAssetListReportItemDetailComponent } from './detail/rou-asset-list-report-item-detail.component';
import { RouAssetListReportItemRoutingModule } from './route/rou-asset-list-report-item-routing.module';

@NgModule({
  imports: [SharedModule, RouAssetListReportItemRoutingModule],
  declarations: [RouAssetListReportItemComponent, RouAssetListReportItemDetailComponent],
})
export class RouAssetListReportItemModule {}
