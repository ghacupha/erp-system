import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouDepreciationPostingReportComponent } from './list/rou-depreciation-posting-report.component';
import { RouDepreciationPostingReportDetailComponent } from './detail/rou-depreciation-posting-report-detail.component';
import { RouDepreciationPostingReportUpdateComponent } from './update/rou-depreciation-posting-report-update.component';
import { RouDepreciationPostingReportDeleteDialogComponent } from './delete/rou-depreciation-posting-report-delete-dialog.component';
import { RouDepreciationPostingReportRoutingModule } from './route/rou-depreciation-posting-report-routing.module';

@NgModule({
  imports: [SharedModule, RouDepreciationPostingReportRoutingModule],
  declarations: [
    RouDepreciationPostingReportComponent,
    RouDepreciationPostingReportDetailComponent,
    RouDepreciationPostingReportUpdateComponent,
    RouDepreciationPostingReportDeleteDialogComponent,
  ],
  entryComponents: [RouDepreciationPostingReportDeleteDialogComponent],
})
export class RouDepreciationPostingReportModule {}
