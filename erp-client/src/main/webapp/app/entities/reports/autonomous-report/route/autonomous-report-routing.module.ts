import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AutonomousReportComponent } from '../list/autonomous-report.component';
import { AutonomousReportDetailComponent } from '../detail/autonomous-report-detail.component';
import { AutonomousReportRoutingResolveService } from './autonomous-report-routing-resolve.service';

const autonomousReportRoute: Routes = [
  {
    path: '',
    component: AutonomousReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AutonomousReportDetailComponent,
    resolve: {
      autonomousReport: AutonomousReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(autonomousReportRoute)],
  exports: [RouterModule],
})
export class AutonomousReportRoutingModule {}
