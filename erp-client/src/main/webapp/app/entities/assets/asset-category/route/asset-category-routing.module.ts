import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetCategoryComponent } from '../list/asset-category.component';
import { AssetCategoryDetailComponent } from '../detail/asset-category-detail.component';
import { AssetCategoryUpdateComponent } from '../update/asset-category-update.component';
import { AssetCategoryRoutingResolveService } from './asset-category-routing-resolve.service';

const assetCategoryRoute: Routes = [
  {
    path: '',
    component: AssetCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetCategoryDetailComponent,
    resolve: {
      assetCategory: AssetCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetCategoryUpdateComponent,
    resolve: {
      assetCategory: AssetCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetCategoryUpdateComponent,
    resolve: {
      assetCategory: AssetCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetCategoryRoute)],
  exports: [RouterModule],
})
export class AssetCategoryRoutingModule {}
