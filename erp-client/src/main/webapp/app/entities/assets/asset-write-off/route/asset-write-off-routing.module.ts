import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetWriteOffComponent } from '../list/asset-write-off.component';
import { AssetWriteOffDetailComponent } from '../detail/asset-write-off-detail.component';
import { AssetWriteOffUpdateComponent } from '../update/asset-write-off-update.component';
import { AssetWriteOffRoutingResolveService } from './asset-write-off-routing-resolve.service';

const assetWriteOffRoute: Routes = [
  {
    path: '',
    component: AssetWriteOffComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetWriteOffDetailComponent,
    resolve: {
      assetWriteOff: AssetWriteOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetWriteOffUpdateComponent,
    resolve: {
      assetWriteOff: AssetWriteOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetWriteOffUpdateComponent,
    resolve: {
      assetWriteOff: AssetWriteOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetWriteOffRoute)],
  exports: [RouterModule],
})
export class AssetWriteOffRoutingModule {}
