import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseRepaymentPeriodComponent } from '../list/lease-repayment-period.component';
import { LeaseRepaymentPeriodDetailComponent } from '../detail/lease-repayment-period-detail.component';
import { LeaseRepaymentPeriodUpdateComponent } from '../update/lease-repayment-period-update.component';
import { LeaseRepaymentPeriodRoutingResolveService } from './lease-repayment-period-routing-resolve.service';

const leaseRepaymentPeriodRoute: Routes = [
  {
    path: '',
    component: LeaseRepaymentPeriodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseRepaymentPeriodDetailComponent,
    resolve: {
      leaseRepaymentPeriod: LeaseRepaymentPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseRepaymentPeriodUpdateComponent,
    resolve: {
      leaseRepaymentPeriod: LeaseRepaymentPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseRepaymentPeriodUpdateComponent,
    resolve: {
      leaseRepaymentPeriod: LeaseRepaymentPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseRepaymentPeriodRoute)],
  exports: [RouterModule],
})
export class LeaseRepaymentPeriodRoutingModule {}
