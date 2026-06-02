import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { KenyanCurrencyDenominationComponent } from '../list/kenyan-currency-denomination.component';
import { KenyanCurrencyDenominationDetailComponent } from '../detail/kenyan-currency-denomination-detail.component';
import { KenyanCurrencyDenominationUpdateComponent } from '../update/kenyan-currency-denomination-update.component';
import { KenyanCurrencyDenominationRoutingResolveService } from './kenyan-currency-denomination-routing-resolve.service';

const kenyanCurrencyDenominationRoute: Routes = [
  {
    path: '',
    component: KenyanCurrencyDenominationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: KenyanCurrencyDenominationDetailComponent,
    resolve: {
      kenyanCurrencyDenomination: KenyanCurrencyDenominationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: KenyanCurrencyDenominationUpdateComponent,
    resolve: {
      kenyanCurrencyDenomination: KenyanCurrencyDenominationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: KenyanCurrencyDenominationUpdateComponent,
    resolve: {
      kenyanCurrencyDenomination: KenyanCurrencyDenominationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(kenyanCurrencyDenominationRoute)],
  exports: [RouterModule],
})
export class KenyanCurrencyDenominationRoutingModule {}
