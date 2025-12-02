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
import { LeasePaymentComponent } from '../list/lease-payment.component';
import { LeasePaymentDetailComponent } from '../detail/lease-payment-detail.component';
import { LeasePaymentUpdateComponent } from '../update/lease-payment-update.component';
import { LeasePaymentRoutingResolveService } from './lease-payment-routing-resolve.service';

const leasePaymentRoute: Routes = [
  {
    path: '',
    component: LeasePaymentComponent,
    data: {
      defaultSort: 'id,desc',
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
