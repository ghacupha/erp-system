import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WIPListReportComponent } from '../list/wip-list-report.component';
import { WIPListReportDetailComponent } from '../detail/wip-list-report-detail.component';
import { WIPListReportUpdateComponent } from '../update/wip-list-report-update.component';
import { WIPListReportRoutingResolveService } from './wip-list-report-routing-resolve.service';

const wIPListReportRoute: Routes = [
  {
    path: '',
    component: WIPListReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WIPListReportDetailComponent,
    resolve: {
      wIPListReport: WIPListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WIPListReportUpdateComponent,
    resolve: {
      wIPListReport: WIPListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WIPListReportUpdateComponent,
    resolve: {
      wIPListReport: WIPListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wIPListReportRoute)],
  exports: [RouterModule],
})
export class WIPListReportRoutingModule {}
