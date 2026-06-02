import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountBalanceComponent } from '../list/account-balance.component';
import { AccountBalanceDetailComponent } from '../detail/account-balance-detail.component';
import { AccountBalanceUpdateComponent } from '../update/account-balance-update.component';
import { AccountBalanceRoutingResolveService } from './account-balance-routing-resolve.service';

const accountBalanceRoute: Routes = [
  {
    path: '',
    component: AccountBalanceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountBalanceDetailComponent,
    resolve: {
      accountBalance: AccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountBalanceUpdateComponent,
    resolve: {
      accountBalance: AccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountBalanceUpdateComponent,
    resolve: {
      accountBalance: AccountBalanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountBalanceRoute)],
  exports: [RouterModule],
})
export class AccountBalanceRoutingModule {}
