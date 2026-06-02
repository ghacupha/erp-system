import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityScheduleReportComponent } from './list/lease-liability-schedule-report.component';
import { LeaseLiabilityScheduleReportDetailComponent } from './detail/lease-liability-schedule-report-detail.component';
import { LeaseLiabilityScheduleReportUpdateComponent } from './update/lease-liability-schedule-report-update.component';
import { LeaseLiabilityScheduleReportDeleteDialogComponent } from './delete/lease-liability-schedule-report-delete-dialog.component';
import { LeaseLiabilityScheduleReportRoutingModule } from './route/lease-liability-schedule-report-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityScheduleReportRoutingModule],
  declarations: [
    LeaseLiabilityScheduleReportComponent,
    LeaseLiabilityScheduleReportDetailComponent,
    LeaseLiabilityScheduleReportUpdateComponent,
    LeaseLiabilityScheduleReportDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityScheduleReportDeleteDialogComponent],
})
export class LeaseLiabilityScheduleReportModule {}
