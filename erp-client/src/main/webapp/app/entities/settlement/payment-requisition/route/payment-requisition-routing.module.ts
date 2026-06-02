import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentRequisitionComponent } from '../list/payment-requisition.component';
import { PaymentRequisitionDetailComponent } from '../detail/payment-requisition-detail.component';
import { PaymentRequisitionUpdateComponent } from '../update/payment-requisition-update.component';
import { PaymentRequisitionRoutingResolveService } from './payment-requisition-routing-resolve.service';

const paymentRequisitionRoute: Routes = [
  {
    path: '',
    component: PaymentRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentRequisitionDetailComponent,
    resolve: {
      paymentRequisition: PaymentRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentRequisitionUpdateComponent,
    resolve: {
      paymentRequisition: PaymentRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentRequisitionUpdateComponent,
    resolve: {
      paymentRequisition: PaymentRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentRequisitionRoute)],
  exports: [RouterModule],
})
export class PaymentRequisitionRoutingModule {}
