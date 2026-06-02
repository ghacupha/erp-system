import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AutonomousReportComponent } from './list/autonomous-report.component';
import { AutonomousReportDetailComponent } from './detail/autonomous-report-detail.component';
import { AutonomousReportRoutingModule } from './route/autonomous-report-routing.module';

@NgModule({
  imports: [SharedModule, AutonomousReportRoutingModule],
  declarations: [AutonomousReportComponent, AutonomousReportDetailComponent],
})
export class AutonomousReportModule {}
