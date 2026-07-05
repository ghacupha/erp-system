import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouDepreciationEntryReportComponent } from './list/rou-depreciation-entry-report.component';
import { RouDepreciationEntryReportDetailComponent } from './detail/rou-depreciation-entry-report-detail.component';
import { RouDepreciationEntryReportUpdateComponent } from './update/rou-depreciation-entry-report-update.component';
import { RouDepreciationEntryReportDeleteDialogComponent } from './delete/rou-depreciation-entry-report-delete-dialog.component';
import { RouDepreciationEntryReportRoutingModule } from './route/rou-depreciation-entry-report-routing.module';

@NgModule({
  imports: [SharedModule, RouDepreciationEntryReportRoutingModule],
  declarations: [
    RouDepreciationEntryReportComponent,
    RouDepreciationEntryReportDetailComponent,
    RouDepreciationEntryReportUpdateComponent,
    RouDepreciationEntryReportDeleteDialogComponent,
  ],
  entryComponents: [RouDepreciationEntryReportDeleteDialogComponent],
})
export class RouDepreciationEntryReportModule {}
