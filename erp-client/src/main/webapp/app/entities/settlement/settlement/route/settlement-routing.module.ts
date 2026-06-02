import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettlementComponent } from '../list/settlement.component';
import { SettlementDetailComponent } from '../detail/settlement-detail.component';
import { SettlementUpdateComponent } from '../update/settlement-update.component';
import { SettlementRoutingResolveService } from './settlement-routing-resolve.service';

const settlementRoute: Routes = [
  {
    path: '',
    component: SettlementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SettlementDetailComponent,
    resolve: {
      settlement: SettlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SettlementUpdateComponent,
    resolve: {
      settlement: SettlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SettlementUpdateComponent,
    resolve: {
      settlement: SettlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(settlementRoute)],
  exports: [RouterModule],
})
export class SettlementRoutingModule {}
