import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkInProgressOverviewComponent } from '../list/work-in-progress-overview.component';
import { WorkInProgressOverviewDetailComponent } from '../detail/work-in-progress-overview-detail.component';
import { WorkInProgressOverviewRoutingResolveService } from './work-in-progress-overview-routing-resolve.service';

const workInProgressOverviewRoute: Routes = [
  {
    path: '',
    component: WorkInProgressOverviewComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkInProgressOverviewDetailComponent,
    resolve: {
      workInProgressOverview: WorkInProgressOverviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workInProgressOverviewRoute)],
  exports: [RouterModule],
})
export class WorkInProgressOverviewRoutingModule {}
