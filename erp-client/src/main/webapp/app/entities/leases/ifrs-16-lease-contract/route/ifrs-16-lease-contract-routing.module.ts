import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IFRS16LeaseContractComponent } from '../list/ifrs-16-lease-contract.component';
import { IFRS16LeaseContractDetailComponent } from '../detail/ifrs-16-lease-contract-detail.component';
import { IFRS16LeaseContractUpdateComponent } from '../update/ifrs-16-lease-contract-update.component';
import { IFRS16LeaseContractRoutingResolveService } from './ifrs-16-lease-contract-routing-resolve.service';

const iFRS16LeaseContractRoute: Routes = [
  {
    path: '',
    component: IFRS16LeaseContractComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IFRS16LeaseContractDetailComponent,
    resolve: {
      iFRS16LeaseContract: IFRS16LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IFRS16LeaseContractUpdateComponent,
    resolve: {
      iFRS16LeaseContract: IFRS16LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IFRS16LeaseContractUpdateComponent,
    resolve: {
      iFRS16LeaseContract: IFRS16LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(iFRS16LeaseContractRoute)],
  exports: [RouterModule],
})
export class IFRS16LeaseContractRoutingModule {}
