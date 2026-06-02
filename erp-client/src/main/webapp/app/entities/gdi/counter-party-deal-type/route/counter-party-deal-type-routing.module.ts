import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CounterPartyDealTypeComponent } from '../list/counter-party-deal-type.component';
import { CounterPartyDealTypeDetailComponent } from '../detail/counter-party-deal-type-detail.component';
import { CounterPartyDealTypeUpdateComponent } from '../update/counter-party-deal-type-update.component';
import { CounterPartyDealTypeRoutingResolveService } from './counter-party-deal-type-routing-resolve.service';

const counterPartyDealTypeRoute: Routes = [
  {
    path: '',
    component: CounterPartyDealTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CounterPartyDealTypeDetailComponent,
    resolve: {
      counterPartyDealType: CounterPartyDealTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CounterPartyDealTypeUpdateComponent,
    resolve: {
      counterPartyDealType: CounterPartyDealTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CounterPartyDealTypeUpdateComponent,
    resolve: {
      counterPartyDealType: CounterPartyDealTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(counterPartyDealTypeRoute)],
  exports: [RouterModule],
})
export class CounterPartyDealTypeRoutingModule {}
