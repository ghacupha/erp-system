import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentAmortizationComponent } from '../list/prepayment-amortization.component';
import { PrepaymentAmortizationDetailComponent } from '../detail/prepayment-amortization-detail.component';
import { PrepaymentAmortizationUpdateComponent } from '../update/prepayment-amortization-update.component';
import { PrepaymentAmortizationRoutingResolveService } from './prepayment-amortization-routing-resolve.service';

const prepaymentAmortizationRoute: Routes = [
  {
    path: '',
    component: PrepaymentAmortizationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentAmortizationDetailComponent,
    resolve: {
      prepaymentAmortization: PrepaymentAmortizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentAmortizationUpdateComponent,
    resolve: {
      prepaymentAmortization: PrepaymentAmortizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentAmortizationUpdateComponent,
    resolve: {
      prepaymentAmortization: PrepaymentAmortizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentAmortizationRoute)],
  exports: [RouterModule],
})
export class PrepaymentAmortizationRoutingModule {}
