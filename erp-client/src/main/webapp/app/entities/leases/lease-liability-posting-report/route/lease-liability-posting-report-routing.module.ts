import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityPostingReportComponent } from '../list/lease-liability-posting-report.component';
import { LeaseLiabilityPostingReportDetailComponent } from '../detail/lease-liability-posting-report-detail.component';
import { LeaseLiabilityPostingReportUpdateComponent } from '../update/lease-liability-posting-report-update.component';
import { LeaseLiabilityPostingReportRoutingResolveService } from './lease-liability-posting-report-routing-resolve.service';

const leaseLiabilityPostingReportRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityPostingReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityPostingReportDetailComponent,
    resolve: {
      leaseLiabilityPostingReport: LeaseLiabilityPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityPostingReportUpdateComponent,
    resolve: {
      leaseLiabilityPostingReport: LeaseLiabilityPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityPostingReportUpdateComponent,
    resolve: {
      leaseLiabilityPostingReport: LeaseLiabilityPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityPostingReportRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityPostingReportRoutingModule {}
