import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouAccountBalanceReportComponent } from '../list/rou-account-balance-report.component';
import { RouAccountBalanceReportDetailComponent } from '../detail/rou-account-balance-report-detail.component';
import { RouAccountBalanceReportUpdateComponent } from '../update/rou-account-balance-report-update.component';
import { RouAccountBalanceReportRoutingResolveService } from './rou-account-balance-report-routing-resolve.service';

const rouAccountBalanceReportRoute: Routes = [
  {
    path: '',
    component: RouAccountBalanceReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouAccountBalanceReportDetailComponent,
    resolve: {
      rouAccountBalanceReport: RouAccountBalanceReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouAccountBalanceReportUpdateComponent,
    resolve: {
      rouAccountBalanceReport: RouAccountBalanceReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouAccountBalanceReportUpdateComponent,
    resolve: {
      rouAccountBalanceReport: RouAccountBalanceReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouAccountBalanceReportRoute)],
  exports: [RouterModule],
})
export class RouAccountBalanceReportRoutingModule {}
