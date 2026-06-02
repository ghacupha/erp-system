import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WIPTransferListItemComponent } from '../list/wip-transfer-list-item.component';
import { WIPTransferListItemDetailComponent } from '../detail/wip-transfer-list-item-detail.component';
import { WIPTransferListItemRoutingResolveService } from './wip-transfer-list-item-routing-resolve.service';

const wIPTransferListItemRoute: Routes = [
  {
    path: '',
    component: WIPTransferListItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WIPTransferListItemDetailComponent,
    resolve: {
      wIPTransferListItem: WIPTransferListItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wIPTransferListItemRoute)],
  exports: [RouterModule],
})
export class WIPTransferListItemRoutingModule {}
