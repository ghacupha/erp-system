import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityByAccountReportComponent } from '../list/lease-liability-by-account-report.component';
import { LeaseLiabilityByAccountReportDetailComponent } from '../detail/lease-liability-by-account-report-detail.component';
import { LeaseLiabilityByAccountReportUpdateComponent } from '../update/lease-liability-by-account-report-update.component';
import { LeaseLiabilityByAccountReportRoutingResolveService } from './lease-liability-by-account-report-routing-resolve.service';

const leaseLiabilityByAccountReportRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityByAccountReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityByAccountReportDetailComponent,
    resolve: {
      leaseLiabilityByAccountReport: LeaseLiabilityByAccountReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityByAccountReportUpdateComponent,
    resolve: {
      leaseLiabilityByAccountReport: LeaseLiabilityByAccountReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityByAccountReportUpdateComponent,
    resolve: {
      leaseLiabilityByAccountReport: LeaseLiabilityByAccountReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityByAccountReportRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityByAccountReportRoutingModule {}
