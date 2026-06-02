import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MonthlyPrepaymentOutstandingReportItemComponent } from '../list/monthly-prepayment-outstanding-report-item.component';
import { MonthlyPrepaymentOutstandingReportItemDetailComponent } from '../detail/monthly-prepayment-outstanding-report-item-detail.component';
import { MonthlyPrepaymentOutstandingReportItemRoutingResolveService } from './monthly-prepayment-outstanding-report-item-routing-resolve.service';

const monthlyPrepaymentOutstandingReportItemRoute: Routes = [
  {
    path: '',
    component: MonthlyPrepaymentOutstandingReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MonthlyPrepaymentOutstandingReportItemDetailComponent,
    resolve: {
      monthlyPrepaymentOutstandingReportItem: MonthlyPrepaymentOutstandingReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(monthlyPrepaymentOutstandingReportItemRoute)],
  exports: [RouterModule],
})
export class MonthlyPrepaymentOutstandingReportItemRoutingModule {}
