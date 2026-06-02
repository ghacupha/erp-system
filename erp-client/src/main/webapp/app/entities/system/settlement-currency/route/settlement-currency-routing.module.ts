import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettlementCurrencyComponent } from '../list/settlement-currency.component';
import { SettlementCurrencyDetailComponent } from '../detail/settlement-currency-detail.component';
import { SettlementCurrencyUpdateComponent } from '../update/settlement-currency-update.component';
import { SettlementCurrencyRoutingResolveService } from './settlement-currency-routing-resolve.service';

const settlementCurrencyRoute: Routes = [
  {
    path: '',
    component: SettlementCurrencyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SettlementCurrencyDetailComponent,
    resolve: {
      settlementCurrency: SettlementCurrencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SettlementCurrencyUpdateComponent,
    resolve: {
      settlementCurrency: SettlementCurrencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SettlementCurrencyUpdateComponent,
    resolve: {
      settlementCurrency: SettlementCurrencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(settlementCurrencyRoute)],
  exports: [RouterModule],
})
export class SettlementCurrencyRoutingModule {}
