import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountOwnershipTypeComponent } from '../list/account-ownership-type.component';
import { AccountOwnershipTypeDetailComponent } from '../detail/account-ownership-type-detail.component';
import { AccountOwnershipTypeRoutingResolveService } from './account-ownership-type-routing-resolve.service';

const accountOwnershipTypeRoute: Routes = [
  {
    path: '',
    component: AccountOwnershipTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountOwnershipTypeDetailComponent,
    resolve: {
      accountOwnershipType: AccountOwnershipTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountOwnershipTypeRoute)],
  exports: [RouterModule],
})
export class AccountOwnershipTypeRoutingModule {}
