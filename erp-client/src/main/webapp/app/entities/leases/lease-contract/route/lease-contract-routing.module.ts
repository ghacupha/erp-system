import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseContractComponent } from '../list/lease-contract.component';
import { LeaseContractDetailComponent } from '../detail/lease-contract-detail.component';
import { LeaseContractUpdateComponent } from '../update/lease-contract-update.component';
import { LeaseContractRoutingResolveService } from './lease-contract-routing-resolve.service';

const leaseContractRoute: Routes = [
  {
    path: '',
    component: LeaseContractComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseContractDetailComponent,
    resolve: {
      leaseContract: LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseContractUpdateComponent,
    resolve: {
      leaseContract: LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseContractUpdateComponent,
    resolve: {
      leaseContract: LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseContractRoute)],
  exports: [RouterModule],
})
export class LeaseContractRoutingModule {}
