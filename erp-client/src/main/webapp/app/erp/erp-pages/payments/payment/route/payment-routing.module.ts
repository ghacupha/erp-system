///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
