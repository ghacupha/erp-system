import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountStatusTypeComponent } from '../list/account-status-type.component';
import { AccountStatusTypeDetailComponent } from '../detail/account-status-type-detail.component';
import { AccountStatusTypeUpdateComponent } from '../update/account-status-type-update.component';
import { AccountStatusTypeRoutingResolveService } from './account-status-type-routing-resolve.service';

const accountStatusTypeRoute: Routes = [
  {
    path: '',
    component: AccountStatusTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountStatusTypeDetailComponent,
    resolve: {
      accountStatusType: AccountStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountStatusTypeUpdateComponent,
    resolve: {
      accountStatusType: AccountStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountStatusTypeUpdateComponent,
    resolve: {
      accountStatusType: AccountStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountStatusTypeRoute)],
  exports: [RouterModule],
})
export class AccountStatusTypeRoutingModule {}
