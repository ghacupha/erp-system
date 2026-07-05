import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouDepreciationEntryReportItemComponent } from './list/rou-depreciation-entry-report-item.component';
import { RouDepreciationEntryReportItemDetailComponent } from './detail/rou-depreciation-entry-report-item-detail.component';
import { RouDepreciationEntryReportItemRoutingModule } from './route/rou-depreciation-entry-report-item-routing.module';

@NgModule({
  imports: [SharedModule, RouDepreciationEntryReportItemRoutingModule],
  declarations: [RouDepreciationEntryReportItemComponent, RouDepreciationEntryReportItemDetailComponent],
})
export class RouDepreciationEntryReportItemModule {}
