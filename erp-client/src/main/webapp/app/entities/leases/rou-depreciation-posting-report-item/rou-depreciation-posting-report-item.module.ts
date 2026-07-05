import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouDepreciationPostingReportItemComponent } from './list/rou-depreciation-posting-report-item.component';
import { RouDepreciationPostingReportItemDetailComponent } from './detail/rou-depreciation-posting-report-item-detail.component';
import { RouDepreciationPostingReportItemRoutingModule } from './route/rou-depreciation-posting-report-item-routing.module';

@NgModule({
  imports: [SharedModule, RouDepreciationPostingReportItemRoutingModule],
  declarations: [RouDepreciationPostingReportItemComponent, RouDepreciationPostingReportItemDetailComponent],
})
export class RouDepreciationPostingReportItemModule {}
