import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WIPTransferListReportComponent } from '../list/wip-transfer-list-report.component';
import { WIPTransferListReportDetailComponent } from '../detail/wip-transfer-list-report-detail.component';
import { WIPTransferListReportUpdateComponent } from '../update/wip-transfer-list-report-update.component';
import { WIPTransferListReportRoutingResolveService } from './wip-transfer-list-report-routing-resolve.service';

const wIPTransferListReportRoute: Routes = [
  {
    path: '',
    component: WIPTransferListReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WIPTransferListReportDetailComponent,
    resolve: {
      wIPTransferListReport: WIPTransferListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WIPTransferListReportUpdateComponent,
    resolve: {
      wIPTransferListReport: WIPTransferListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WIPTransferListReportUpdateComponent,
    resolve: {
      wIPTransferListReport: WIPTransferListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wIPTransferListReportRoute)],
  exports: [RouterModule],
})
export class WIPTransferListReportRoutingModule {}
