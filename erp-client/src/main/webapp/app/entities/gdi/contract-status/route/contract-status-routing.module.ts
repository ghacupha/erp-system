import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContractStatusComponent } from '../list/contract-status.component';
import { ContractStatusDetailComponent } from '../detail/contract-status-detail.component';
import { ContractStatusUpdateComponent } from '../update/contract-status-update.component';
import { ContractStatusRoutingResolveService } from './contract-status-routing-resolve.service';

const contractStatusRoute: Routes = [
  {
    path: '',
    component: ContractStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractStatusDetailComponent,
    resolve: {
      contractStatus: ContractStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractStatusUpdateComponent,
    resolve: {
      contractStatus: ContractStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractStatusUpdateComponent,
    resolve: {
      contractStatus: ContractStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contractStatusRoute)],
  exports: [RouterModule],
})
export class ContractStatusRoutingModule {}
