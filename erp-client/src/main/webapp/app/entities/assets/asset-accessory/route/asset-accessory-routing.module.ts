import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetAccessoryComponent } from '../list/asset-accessory.component';
import { AssetAccessoryDetailComponent } from '../detail/asset-accessory-detail.component';
import { AssetAccessoryUpdateComponent } from '../update/asset-accessory-update.component';
import { AssetAccessoryRoutingResolveService } from './asset-accessory-routing-resolve.service';

const assetAccessoryRoute: Routes = [
  {
    path: '',
    component: AssetAccessoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetAccessoryDetailComponent,
    resolve: {
      assetAccessory: AssetAccessoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetAccessoryUpdateComponent,
    resolve: {
      assetAccessory: AssetAccessoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetAccessoryUpdateComponent,
    resolve: {
      assetAccessory: AssetAccessoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetAccessoryRoute)],
  exports: [RouterModule],
})
export class AssetAccessoryRoutingModule {}
