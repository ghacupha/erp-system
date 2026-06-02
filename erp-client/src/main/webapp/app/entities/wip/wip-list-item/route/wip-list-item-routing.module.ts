import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WIPListItemComponent } from '../list/wip-list-item.component';
import { WIPListItemDetailComponent } from '../detail/wip-list-item-detail.component';
import { WIPListItemRoutingResolveService } from './wip-list-item-routing-resolve.service';

const wIPListItemRoute: Routes = [
  {
    path: '',
    component: WIPListItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WIPListItemDetailComponent,
    resolve: {
      wIPListItem: WIPListItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wIPListItemRoute)],
  exports: [RouterModule],
})
export class WIPListItemRoutingModule {}
