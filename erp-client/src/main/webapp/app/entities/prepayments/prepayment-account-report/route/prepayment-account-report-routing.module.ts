import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentAccountReportComponent } from '../list/prepayment-account-report.component';
import { PrepaymentAccountReportDetailComponent } from '../detail/prepayment-account-report-detail.component';
import { PrepaymentAccountReportRoutingResolveService } from './prepayment-account-report-routing-resolve.service';

const prepaymentAccountReportRoute: Routes = [
  {
    path: '',
    component: PrepaymentAccountReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentAccountReportDetailComponent,
    resolve: {
      prepaymentAccountReport: PrepaymentAccountReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentAccountReportRoute)],
  exports: [RouterModule],
})
export class PrepaymentAccountReportRoutingModule {}
