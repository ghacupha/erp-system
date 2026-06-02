import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbAccountHolderTypeComponent } from '../list/crb-account-holder-type.component';
import { CrbAccountHolderTypeDetailComponent } from '../detail/crb-account-holder-type-detail.component';
import { CrbAccountHolderTypeUpdateComponent } from '../update/crb-account-holder-type-update.component';
import { CrbAccountHolderTypeRoutingResolveService } from './crb-account-holder-type-routing-resolve.service';

const crbAccountHolderTypeRoute: Routes = [
  {
    path: '',
    component: CrbAccountHolderTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbAccountHolderTypeDetailComponent,
    resolve: {
      crbAccountHolderType: CrbAccountHolderTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbAccountHolderTypeUpdateComponent,
    resolve: {
      crbAccountHolderType: CrbAccountHolderTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbAccountHolderTypeUpdateComponent,
    resolve: {
      crbAccountHolderType: CrbAccountHolderTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbAccountHolderTypeRoute)],
  exports: [RouterModule],
})
export class CrbAccountHolderTypeRoutingModule {}
