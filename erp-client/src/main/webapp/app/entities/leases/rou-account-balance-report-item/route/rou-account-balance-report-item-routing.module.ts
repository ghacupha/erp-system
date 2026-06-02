import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouAccountBalanceReportItemComponent } from '../list/rou-account-balance-report-item.component';
import { RouAccountBalanceReportItemDetailComponent } from '../detail/rou-account-balance-report-item-detail.component';
import { RouAccountBalanceReportItemRoutingResolveService } from './rou-account-balance-report-item-routing-resolve.service';

const rouAccountBalanceReportItemRoute: Routes = [
  {
    path: '',
    component: RouAccountBalanceReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouAccountBalanceReportItemDetailComponent,
    resolve: {
      rouAccountBalanceReportItem: RouAccountBalanceReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouAccountBalanceReportItemRoute)],
  exports: [RouterModule],
})
export class RouAccountBalanceReportItemRoutingModule {}
