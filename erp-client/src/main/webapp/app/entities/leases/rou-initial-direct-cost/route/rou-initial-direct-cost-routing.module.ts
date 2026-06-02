import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouInitialDirectCostComponent } from '../list/rou-initial-direct-cost.component';
import { RouInitialDirectCostDetailComponent } from '../detail/rou-initial-direct-cost-detail.component';
import { RouInitialDirectCostUpdateComponent } from '../update/rou-initial-direct-cost-update.component';
import { RouInitialDirectCostRoutingResolveService } from './rou-initial-direct-cost-routing-resolve.service';

const rouInitialDirectCostRoute: Routes = [
  {
    path: '',
    component: RouInitialDirectCostComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouInitialDirectCostDetailComponent,
    resolve: {
      rouInitialDirectCost: RouInitialDirectCostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouInitialDirectCostUpdateComponent,
    resolve: {
      rouInitialDirectCost: RouInitialDirectCostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouInitialDirectCostUpdateComponent,
    resolve: {
      rouInitialDirectCost: RouInitialDirectCostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouInitialDirectCostRoute)],
  exports: [RouterModule],
})
export class RouInitialDirectCostRoutingModule {}
