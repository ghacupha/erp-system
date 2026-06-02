import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountTypeComponent } from '../list/account-type.component';
import { AccountTypeDetailComponent } from '../detail/account-type-detail.component';
import { AccountTypeUpdateComponent } from '../update/account-type-update.component';
import { AccountTypeRoutingResolveService } from './account-type-routing-resolve.service';

const accountTypeRoute: Routes = [
  {
    path: '',
    component: AccountTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountTypeDetailComponent,
    resolve: {
      accountType: AccountTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountTypeUpdateComponent,
    resolve: {
      accountType: AccountTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountTypeUpdateComponent,
    resolve: {
      accountType: AccountTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountTypeRoute)],
  exports: [RouterModule],
})
export class AccountTypeRoutingModule {}
