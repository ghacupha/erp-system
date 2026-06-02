import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentCalculationComponent } from '../list/payment-calculation.component';
import { PaymentCalculationDetailComponent } from '../detail/payment-calculation-detail.component';
import { PaymentCalculationUpdateComponent } from '../update/payment-calculation-update.component';
import { PaymentCalculationRoutingResolveService } from './payment-calculation-routing-resolve.service';

const paymentCalculationRoute: Routes = [
  {
    path: '',
    component: PaymentCalculationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentCalculationDetailComponent,
    resolve: {
      paymentCalculation: PaymentCalculationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentCalculationUpdateComponent,
    resolve: {
      paymentCalculation: PaymentCalculationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentCalculationUpdateComponent,
    resolve: {
      paymentCalculation: PaymentCalculationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentCalculationRoute)],
  exports: [RouterModule],
})
export class PaymentCalculationRoutingModule {}
