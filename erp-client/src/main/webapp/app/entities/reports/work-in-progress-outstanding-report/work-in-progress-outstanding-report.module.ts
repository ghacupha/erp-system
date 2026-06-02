import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkInProgressOutstandingReportComponent } from './list/work-in-progress-outstanding-report.component';
import { WorkInProgressOutstandingReportDetailComponent } from './detail/work-in-progress-outstanding-report-detail.component';
import { WorkInProgressOutstandingReportRoutingModule } from './route/work-in-progress-outstanding-report-routing.module';

@NgModule({
  imports: [SharedModule, WorkInProgressOutstandingReportRoutingModule],
  declarations: [WorkInProgressOutstandingReportComponent, WorkInProgressOutstandingReportDetailComponent],
})
export class WorkInProgressOutstandingReportModule {}
