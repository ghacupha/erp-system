import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentComponent } from '../list/payment.component';
import { PaymentDetailComponent } from '../detail/payment-detail.component';
import { PaymentUpdateComponent } from '../update/payment-update.component';
import { PaymentRoutingResolveService } from './payment-routing-resolve.service';
import {NewPaymentResolve} from "./new-payment-resolve.service";
import {EditPaymentResolve} from "./edit-payment-resolve.service";
import {CopyPaymentResolve} from "./copy-payment-resolve.service";
import {Authority} from "../../../../../config/authority.constants";

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
      // payment: PaymentRoutingResolveService,
      payment: NewPaymentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ERP| New Payment',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentUpdateComponent,
    resolve: {
      // payment: PaymentRoutingResolveService,
      payment: EditPaymentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ERP| Edit Payment',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/copy',
    component: PaymentUpdateComponent,
    resolve: {
      // payment: PaymentRoutingResolveService,
      payment: CopyPaymentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ERP| Copy Payment',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentRoute)],
  exports: [RouterModule],
})
export class PaymentRoutingModule {}
