import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationReportComponent } from './list/depreciation-report.component';
import { DepreciationReportDetailComponent } from './detail/depreciation-report-detail.component';
import { DepreciationReportUpdateComponent } from './update/depreciation-report-update.component';
import { DepreciationReportDeleteDialogComponent } from './delete/depreciation-report-delete-dialog.component';
import { DepreciationReportRoutingModule } from './route/depreciation-report-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationReportRoutingModule],
  declarations: [
    DepreciationReportComponent,
    DepreciationReportDetailComponent,
    DepreciationReportUpdateComponent,
    DepreciationReportDeleteDialogComponent,
  ],
  entryComponents: [DepreciationReportDeleteDialogComponent],
})
export class DepreciationReportModule {}
