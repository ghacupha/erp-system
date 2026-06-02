import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkInProgressReportComponent } from '../list/work-in-progress-report.component';
import { WorkInProgressReportDetailComponent } from '../detail/work-in-progress-report-detail.component';
import { WorkInProgressReportRoutingResolveService } from './work-in-progress-report-routing-resolve.service';

const workInProgressReportRoute: Routes = [
  {
    path: '',
    component: WorkInProgressReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkInProgressReportDetailComponent,
    resolve: {
      workInProgressReport: WorkInProgressReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workInProgressReportRoute)],
  exports: [RouterModule],
})
export class WorkInProgressReportRoutingModule {}
