import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkInProgressReportComponent } from './list/work-in-progress-report.component';
import { WorkInProgressReportDetailComponent } from './detail/work-in-progress-report-detail.component';
import { WorkInProgressReportRoutingModule } from './route/work-in-progress-report-routing.module';

@NgModule({
  imports: [SharedModule, WorkInProgressReportRoutingModule],
  declarations: [WorkInProgressReportComponent, WorkInProgressReportDetailComponent],
})
export class WorkInProgressReportModule {}
