import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentOutstandingOverviewReportComponent } from '../list/prepayment-outstanding-overview-report.component';
import { PrepaymentOutstandingOverviewReportDetailComponent } from '../detail/prepayment-outstanding-overview-report-detail.component';
import { PrepaymentOutstandingOverviewReportRoutingResolveService } from './prepayment-outstanding-overview-report-routing-resolve.service';

const prepaymentOutstandingOverviewReportRoute: Routes = [
  {
    path: '',
    component: PrepaymentOutstandingOverviewReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentOutstandingOverviewReportDetailComponent,
    resolve: {
      prepaymentOutstandingOverviewReport: PrepaymentOutstandingOverviewReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentOutstandingOverviewReportRoute)],
  exports: [RouterModule],
})
export class PrepaymentOutstandingOverviewReportRoutingModule {}
