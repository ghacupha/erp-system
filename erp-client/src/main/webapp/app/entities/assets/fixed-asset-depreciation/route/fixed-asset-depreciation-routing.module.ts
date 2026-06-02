import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FixedAssetDepreciationComponent } from '../list/fixed-asset-depreciation.component';
import { FixedAssetDepreciationDetailComponent } from '../detail/fixed-asset-depreciation-detail.component';
import { FixedAssetDepreciationUpdateComponent } from '../update/fixed-asset-depreciation-update.component';
import { FixedAssetDepreciationRoutingResolveService } from './fixed-asset-depreciation-routing-resolve.service';

const fixedAssetDepreciationRoute: Routes = [
  {
    path: '',
    component: FixedAssetDepreciationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FixedAssetDepreciationDetailComponent,
    resolve: {
      fixedAssetDepreciation: FixedAssetDepreciationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FixedAssetDepreciationUpdateComponent,
    resolve: {
      fixedAssetDepreciation: FixedAssetDepreciationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FixedAssetDepreciationUpdateComponent,
    resolve: {
      fixedAssetDepreciation: FixedAssetDepreciationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fixedAssetDepreciationRoute)],
  exports: [RouterModule],
})
export class FixedAssetDepreciationRoutingModule {}
