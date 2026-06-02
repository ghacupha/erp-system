import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExchangeRateComponent } from '../list/exchange-rate.component';
import { ExchangeRateDetailComponent } from '../detail/exchange-rate-detail.component';
import { ExchangeRateUpdateComponent } from '../update/exchange-rate-update.component';
import { ExchangeRateRoutingResolveService } from './exchange-rate-routing-resolve.service';

const exchangeRateRoute: Routes = [
  {
    path: '',
    component: ExchangeRateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExchangeRateDetailComponent,
    resolve: {
      exchangeRate: ExchangeRateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExchangeRateUpdateComponent,
    resolve: {
      exchangeRate: ExchangeRateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExchangeRateUpdateComponent,
    resolve: {
      exchangeRate: ExchangeRateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(exchangeRateRoute)],
  exports: [RouterModule],
})
export class ExchangeRateRoutingModule {}
