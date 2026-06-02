import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkInProgressOutstandingReportComponent } from '../list/work-in-progress-outstanding-report.component';
import { WorkInProgressOutstandingReportDetailComponent } from '../detail/work-in-progress-outstanding-report-detail.component';
import { WorkInProgressOutstandingReportRoutingResolveService } from './work-in-progress-outstanding-report-routing-resolve.service';

const workInProgressOutstandingReportRoute: Routes = [
  {
    path: '',
    component: WorkInProgressOutstandingReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkInProgressOutstandingReportDetailComponent,
    resolve: {
      workInProgressOutstandingReport: WorkInProgressOutstandingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workInProgressOutstandingReportRoute)],
  exports: [RouterModule],
})
export class WorkInProgressOutstandingReportRoutingModule {}
