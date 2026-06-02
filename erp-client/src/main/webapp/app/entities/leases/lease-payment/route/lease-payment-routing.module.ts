import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeasePaymentComponent } from '../list/lease-payment.component';
import { LeasePaymentDetailComponent } from '../detail/lease-payment-detail.component';
import { LeasePaymentUpdateComponent } from '../update/lease-payment-update.component';
import { LeasePaymentRoutingResolveService } from './lease-payment-routing-resolve.service';

const leasePaymentRoute: Routes = [
  {
    path: '',
    component: LeasePaymentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeasePaymentDetailComponent,
    resolve: {
      leasePayment: LeasePaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeasePaymentUpdateComponent,
    resolve: {
      leasePayment: LeasePaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeasePaymentUpdateComponent,
    resolve: {
      leasePayment: LeasePaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leasePaymentRoute)],
  exports: [RouterModule],
})
export class LeasePaymentRoutingModule {}
