import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CounterpartyTypeComponent } from '../list/counterparty-type.component';
import { CounterpartyTypeDetailComponent } from '../detail/counterparty-type-detail.component';
import { CounterpartyTypeUpdateComponent } from '../update/counterparty-type-update.component';
import { CounterpartyTypeRoutingResolveService } from './counterparty-type-routing-resolve.service';

const counterpartyTypeRoute: Routes = [
  {
    path: '',
    component: CounterpartyTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CounterpartyTypeDetailComponent,
    resolve: {
      counterpartyType: CounterpartyTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CounterpartyTypeUpdateComponent,
    resolve: {
      counterpartyType: CounterpartyTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CounterpartyTypeUpdateComponent,
    resolve: {
      counterpartyType: CounterpartyTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(counterpartyTypeRoute)],
  exports: [RouterModule],
})
export class CounterpartyTypeRoutingModule {}
