import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SignedPaymentComponent } from '../list/signed-payment.component';
import { SignedPaymentDetailComponent } from '../detail/signed-payment-detail.component';
import { SignedPaymentUpdateComponent } from '../update/signed-payment-update.component';
import { SignedPaymentRoutingResolveService } from './signed-payment-routing-resolve.service';

const signedPaymentRoute: Routes = [
  {
    path: '',
    component: SignedPaymentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SignedPaymentDetailComponent,
    resolve: {
      signedPayment: SignedPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SignedPaymentUpdateComponent,
    resolve: {
      signedPayment: SignedPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SignedPaymentUpdateComponent,
    resolve: {
      signedPayment: SignedPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(signedPaymentRoute)],
  exports: [RouterModule],
})
export class SignedPaymentRoutingModule {}
