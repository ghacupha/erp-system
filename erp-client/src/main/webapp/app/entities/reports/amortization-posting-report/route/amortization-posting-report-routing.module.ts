import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmortizationPostingReportComponent } from '../list/amortization-posting-report.component';
import { AmortizationPostingReportDetailComponent } from '../detail/amortization-posting-report-detail.component';
import { AmortizationPostingReportRoutingResolveService } from './amortization-posting-report-routing-resolve.service';

const amortizationPostingReportRoute: Routes = [
  {
    path: '',
    component: AmortizationPostingReportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmortizationPostingReportDetailComponent,
    resolve: {
      amortizationPostingReport: AmortizationPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amortizationPostingReportRoute)],
  exports: [RouterModule],
})
export class AmortizationPostingReportRoutingModule {}
