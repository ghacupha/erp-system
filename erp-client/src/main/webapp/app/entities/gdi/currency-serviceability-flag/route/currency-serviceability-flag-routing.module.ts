import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CurrencyServiceabilityFlagComponent } from '../list/currency-serviceability-flag.component';
import { CurrencyServiceabilityFlagDetailComponent } from '../detail/currency-serviceability-flag-detail.component';
import { CurrencyServiceabilityFlagUpdateComponent } from '../update/currency-serviceability-flag-update.component';
import { CurrencyServiceabilityFlagRoutingResolveService } from './currency-serviceability-flag-routing-resolve.service';

const currencyServiceabilityFlagRoute: Routes = [
  {
    path: '',
    component: CurrencyServiceabilityFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurrencyServiceabilityFlagDetailComponent,
    resolve: {
      currencyServiceabilityFlag: CurrencyServiceabilityFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CurrencyServiceabilityFlagUpdateComponent,
    resolve: {
      currencyServiceabilityFlag: CurrencyServiceabilityFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CurrencyServiceabilityFlagUpdateComponent,
    resolve: {
      currencyServiceabilityFlag: CurrencyServiceabilityFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(currencyServiceabilityFlagRoute)],
  exports: [RouterModule],
})
export class CurrencyServiceabilityFlagRoutingModule {}
