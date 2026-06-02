import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CurrencyAuthenticityFlagComponent } from '../list/currency-authenticity-flag.component';
import { CurrencyAuthenticityFlagDetailComponent } from '../detail/currency-authenticity-flag-detail.component';
import { CurrencyAuthenticityFlagUpdateComponent } from '../update/currency-authenticity-flag-update.component';
import { CurrencyAuthenticityFlagRoutingResolveService } from './currency-authenticity-flag-routing-resolve.service';

const currencyAuthenticityFlagRoute: Routes = [
  {
    path: '',
    component: CurrencyAuthenticityFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurrencyAuthenticityFlagDetailComponent,
    resolve: {
      currencyAuthenticityFlag: CurrencyAuthenticityFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CurrencyAuthenticityFlagUpdateComponent,
    resolve: {
      currencyAuthenticityFlag: CurrencyAuthenticityFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CurrencyAuthenticityFlagUpdateComponent,
    resolve: {
      currencyAuthenticityFlag: CurrencyAuthenticityFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(currencyAuthenticityFlagRoute)],
  exports: [RouterModule],
})
export class CurrencyAuthenticityFlagRoutingModule {}
