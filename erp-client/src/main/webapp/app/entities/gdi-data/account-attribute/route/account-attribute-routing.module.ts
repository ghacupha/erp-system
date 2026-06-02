import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountAttributeComponent } from '../list/account-attribute.component';
import { AccountAttributeDetailComponent } from '../detail/account-attribute-detail.component';
import { AccountAttributeUpdateComponent } from '../update/account-attribute-update.component';
import { AccountAttributeRoutingResolveService } from './account-attribute-routing-resolve.service';

const accountAttributeRoute: Routes = [
  {
    path: '',
    component: AccountAttributeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountAttributeDetailComponent,
    resolve: {
      accountAttribute: AccountAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountAttributeUpdateComponent,
    resolve: {
      accountAttribute: AccountAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountAttributeUpdateComponent,
    resolve: {
      accountAttribute: AccountAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountAttributeRoute)],
  exports: [RouterModule],
})
export class AccountAttributeRoutingModule {}
