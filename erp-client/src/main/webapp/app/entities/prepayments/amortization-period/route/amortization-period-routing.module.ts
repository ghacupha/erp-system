import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmortizationPeriodComponent } from '../list/amortization-period.component';
import { AmortizationPeriodDetailComponent } from '../detail/amortization-period-detail.component';
import { AmortizationPeriodUpdateComponent } from '../update/amortization-period-update.component';
import { AmortizationPeriodRoutingResolveService } from './amortization-period-routing-resolve.service';

const amortizationPeriodRoute: Routes = [
  {
    path: '',
    component: AmortizationPeriodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmortizationPeriodDetailComponent,
    resolve: {
      amortizationPeriod: AmortizationPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmortizationPeriodUpdateComponent,
    resolve: {
      amortizationPeriod: AmortizationPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmortizationPeriodUpdateComponent,
    resolve: {
      amortizationPeriod: AmortizationPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amortizationPeriodRoute)],
  exports: [RouterModule],
})
export class AmortizationPeriodRoutingModule {}
