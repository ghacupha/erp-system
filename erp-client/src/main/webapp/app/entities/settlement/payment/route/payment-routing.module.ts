import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentComponent } from '../list/payment.component';
import { PaymentDetailComponent } from '../detail/payment-detail.component';
import { PaymentUpdateComponent } from '../update/payment-update.component';
import { PaymentRoutingResolveService } from './payment-routing-resolve.service';

const paymentRoute: Routes = [
  {
    path: '',
    component: PaymentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentDetailComponent,
    resolve: {
      payment: PaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentUpdateComponent,
    resolve: {
      payment: PaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentUpdateComponent,
    resolve: {
      payment: PaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentRoute)],
  exports: [RouterModule],
})
export class PaymentRoutingModule {}
