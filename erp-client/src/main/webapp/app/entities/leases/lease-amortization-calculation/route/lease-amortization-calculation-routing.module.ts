import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseAmortizationCalculationComponent } from '../list/lease-amortization-calculation.component';
import { LeaseAmortizationCalculationDetailComponent } from '../detail/lease-amortization-calculation-detail.component';
import { LeaseAmortizationCalculationUpdateComponent } from '../update/lease-amortization-calculation-update.component';
import { LeaseAmortizationCalculationRoutingResolveService } from './lease-amortization-calculation-routing-resolve.service';

const leaseAmortizationCalculationRoute: Routes = [
  {
    path: '',
    component: LeaseAmortizationCalculationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseAmortizationCalculationDetailComponent,
    resolve: {
      leaseAmortizationCalculation: LeaseAmortizationCalculationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseAmortizationCalculationUpdateComponent,
    resolve: {
      leaseAmortizationCalculation: LeaseAmortizationCalculationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseAmortizationCalculationUpdateComponent,
    resolve: {
      leaseAmortizationCalculation: LeaseAmortizationCalculationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseAmortizationCalculationRoute)],
  exports: [RouterModule],
})
export class LeaseAmortizationCalculationRoutingModule {}
