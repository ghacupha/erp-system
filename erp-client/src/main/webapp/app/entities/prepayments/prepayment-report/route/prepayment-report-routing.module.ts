import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentReportComponent } from '../list/prepayment-report.component';
import { PrepaymentReportDetailComponent } from '../detail/prepayment-report-detail.component';
import { PrepaymentReportRoutingResolveService } from './prepayment-report-routing-resolve.service';

const prepaymentReportRoute: Routes = [
  {
    path: '',
    component: PrepaymentReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentReportDetailComponent,
    resolve: {
      prepaymentReport: PrepaymentReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentReportRoute)],
  exports: [RouterModule],
})
export class PrepaymentReportRoutingModule {}
