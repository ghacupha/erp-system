import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityReportComponent } from '../list/lease-liability-report.component';
import { LeaseLiabilityReportDetailComponent } from '../detail/lease-liability-report-detail.component';
import { LeaseLiabilityReportUpdateComponent } from '../update/lease-liability-report-update.component';
import { LeaseLiabilityReportRoutingResolveService } from './lease-liability-report-routing-resolve.service';

const leaseLiabilityReportRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityReportDetailComponent,
    resolve: {
      leaseLiabilityReport: LeaseLiabilityReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityReportUpdateComponent,
    resolve: {
      leaseLiabilityReport: LeaseLiabilityReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityReportUpdateComponent,
    resolve: {
      leaseLiabilityReport: LeaseLiabilityReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityReportRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityReportRoutingModule {}
