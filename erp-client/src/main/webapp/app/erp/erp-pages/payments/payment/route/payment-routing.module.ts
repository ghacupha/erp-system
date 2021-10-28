import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentComponent } from '../list/payment.component';
import { PaymentDetailComponent } from '../detail/payment-detail.component';
import { PaymentUpdateComponent } from '../update/payment-update.component';
import { PaymentRoutingResolveService } from './payment-routing-resolve.service';
import {Authority} from "../../../../../config/authority.constants";
import {NewPaymentResolveService} from "./new-payment-resolve.service";
import {EditPaymentResolveService} from "./edit-payment-resolve.service";
import {CopyPaymentResolveService} from "./copy-payment-resolve.service";
import {DealerPaymentResolveService} from "./dealer-payment-resolve.service";
import {DealerInvoicePaymentResolveService} from "./dealer-invoice-payment-resolve.service";

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
      payment: NewPaymentResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ERP| New Payment',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'dealer',
    component: PaymentUpdateComponent,
    resolve: {
      payment: DealerPaymentResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ERP| Pay Dealer',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'dealer/invoice',
    component: PaymentUpdateComponent,
    resolve: {
      payment: DealerInvoicePaymentResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ERP| Pay Dealer',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentUpdateComponent,
    resolve: {
      // payment: PaymentRoutingResolveService,
      payment: EditPaymentResolveService,
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
      payment: CopyPaymentResolveService,
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
