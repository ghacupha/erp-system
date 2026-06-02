import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportStatusComponent } from '../list/report-status.component';
import { ReportStatusDetailComponent } from '../detail/report-status-detail.component';
import { ReportStatusUpdateComponent } from '../update/report-status-update.component';
import { ReportStatusRoutingResolveService } from './report-status-routing-resolve.service';

const reportStatusRoute: Routes = [
  {
    path: '',
    component: ReportStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportStatusDetailComponent,
    resolve: {
      reportStatus: ReportStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportStatusUpdateComponent,
    resolve: {
      reportStatus: ReportStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportStatusUpdateComponent,
    resolve: {
      reportStatus: ReportStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportStatusRoute)],
  exports: [RouterModule],
})
export class ReportStatusRoutingModule {}
