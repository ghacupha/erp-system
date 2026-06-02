import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouMonthlyDepreciationReportComponent } from './list/rou-monthly-depreciation-report.component';
import { RouMonthlyDepreciationReportDetailComponent } from './detail/rou-monthly-depreciation-report-detail.component';
import { RouMonthlyDepreciationReportUpdateComponent } from './update/rou-monthly-depreciation-report-update.component';
import { RouMonthlyDepreciationReportDeleteDialogComponent } from './delete/rou-monthly-depreciation-report-delete-dialog.component';
import { RouMonthlyDepreciationReportRoutingModule } from './route/rou-monthly-depreciation-report-routing.module';

@NgModule({
  imports: [SharedModule, RouMonthlyDepreciationReportRoutingModule],
  declarations: [
    RouMonthlyDepreciationReportComponent,
    RouMonthlyDepreciationReportDetailComponent,
    RouMonthlyDepreciationReportUpdateComponent,
    RouMonthlyDepreciationReportDeleteDialogComponent,
  ],
  entryComponents: [RouMonthlyDepreciationReportDeleteDialogComponent],
})
export class RouMonthlyDepreciationReportModule {}
