import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentInvoiceComponent } from '../list/payment-invoice.component';
import { PaymentInvoiceDetailComponent } from '../detail/payment-invoice-detail.component';
import { PaymentInvoiceUpdateComponent } from '../update/payment-invoice-update.component';
import { PaymentInvoiceRoutingResolveService } from './payment-invoice-routing-resolve.service';

const paymentInvoiceRoute: Routes = [
  {
    path: '',
    component: PaymentInvoiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentInvoiceDetailComponent,
    resolve: {
      paymentInvoice: PaymentInvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentInvoiceUpdateComponent,
    resolve: {
      paymentInvoice: PaymentInvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentInvoiceUpdateComponent,
    resolve: {
      paymentInvoice: PaymentInvoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentInvoiceRoute)],
  exports: [RouterModule],
})
export class PaymentInvoiceRoutingModule {}
