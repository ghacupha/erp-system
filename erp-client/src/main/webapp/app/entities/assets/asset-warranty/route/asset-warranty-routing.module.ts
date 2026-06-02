import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetWarrantyComponent } from '../list/asset-warranty.component';
import { AssetWarrantyDetailComponent } from '../detail/asset-warranty-detail.component';
import { AssetWarrantyUpdateComponent } from '../update/asset-warranty-update.component';
import { AssetWarrantyRoutingResolveService } from './asset-warranty-routing-resolve.service';

const assetWarrantyRoute: Routes = [
  {
    path: '',
    component: AssetWarrantyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetWarrantyDetailComponent,
    resolve: {
      assetWarranty: AssetWarrantyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetWarrantyUpdateComponent,
    resolve: {
      assetWarranty: AssetWarrantyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetWarrantyUpdateComponent,
    resolve: {
      assetWarranty: AssetWarrantyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetWarrantyRoute)],
  exports: [RouterModule],
})
export class AssetWarrantyRoutingModule {}
