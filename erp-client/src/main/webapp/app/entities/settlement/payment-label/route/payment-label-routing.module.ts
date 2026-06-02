import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentLabelComponent } from '../list/payment-label.component';
import { PaymentLabelDetailComponent } from '../detail/payment-label-detail.component';
import { PaymentLabelUpdateComponent } from '../update/payment-label-update.component';
import { PaymentLabelRoutingResolveService } from './payment-label-routing-resolve.service';

const paymentLabelRoute: Routes = [
  {
    path: '',
    component: PaymentLabelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentLabelDetailComponent,
    resolve: {
      paymentLabel: PaymentLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentLabelUpdateComponent,
    resolve: {
      paymentLabel: PaymentLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentLabelUpdateComponent,
    resolve: {
      paymentLabel: PaymentLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentLabelRoute)],
  exports: [RouterModule],
})
export class PaymentLabelRoutingModule {}
