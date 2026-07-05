import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouMonthlyDepreciationReportItemComponent } from './list/rou-monthly-depreciation-report-item.component';
import { RouMonthlyDepreciationReportItemDetailComponent } from './detail/rou-monthly-depreciation-report-item-detail.component';
import { RouMonthlyDepreciationReportItemUpdateComponent } from './update/rou-monthly-depreciation-report-item-update.component';
import { RouMonthlyDepreciationReportItemDeleteDialogComponent } from './delete/rou-monthly-depreciation-report-item-delete-dialog.component';
import { RouMonthlyDepreciationReportItemRoutingModule } from './route/rou-monthly-depreciation-report-item-routing.module';

@NgModule({
  imports: [SharedModule, RouMonthlyDepreciationReportItemRoutingModule],
  declarations: [
    RouMonthlyDepreciationReportItemComponent,
    RouMonthlyDepreciationReportItemDetailComponent,
    RouMonthlyDepreciationReportItemUpdateComponent,
    RouMonthlyDepreciationReportItemDeleteDialogComponent,
  ],
  entryComponents: [RouMonthlyDepreciationReportItemDeleteDialogComponent],
})
export class RouMonthlyDepreciationReportItemModule {}
