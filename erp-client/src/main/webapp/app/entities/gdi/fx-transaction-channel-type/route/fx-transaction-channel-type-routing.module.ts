import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FxTransactionChannelTypeComponent } from '../list/fx-transaction-channel-type.component';
import { FxTransactionChannelTypeDetailComponent } from '../detail/fx-transaction-channel-type-detail.component';
import { FxTransactionChannelTypeUpdateComponent } from '../update/fx-transaction-channel-type-update.component';
import { FxTransactionChannelTypeRoutingResolveService } from './fx-transaction-channel-type-routing-resolve.service';

const fxTransactionChannelTypeRoute: Routes = [
  {
    path: '',
    component: FxTransactionChannelTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FxTransactionChannelTypeDetailComponent,
    resolve: {
      fxTransactionChannelType: FxTransactionChannelTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FxTransactionChannelTypeUpdateComponent,
    resolve: {
      fxTransactionChannelType: FxTransactionChannelTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FxTransactionChannelTypeUpdateComponent,
    resolve: {
      fxTransactionChannelType: FxTransactionChannelTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fxTransactionChannelTypeRoute)],
  exports: [RouterModule],
})
export class FxTransactionChannelTypeRoutingModule {}
