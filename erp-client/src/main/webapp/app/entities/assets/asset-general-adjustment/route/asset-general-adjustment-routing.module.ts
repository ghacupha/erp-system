import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetGeneralAdjustmentComponent } from '../list/asset-general-adjustment.component';
import { AssetGeneralAdjustmentDetailComponent } from '../detail/asset-general-adjustment-detail.component';
import { AssetGeneralAdjustmentUpdateComponent } from '../update/asset-general-adjustment-update.component';
import { AssetGeneralAdjustmentRoutingResolveService } from './asset-general-adjustment-routing-resolve.service';

const assetGeneralAdjustmentRoute: Routes = [
  {
    path: '',
    component: AssetGeneralAdjustmentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetGeneralAdjustmentDetailComponent,
    resolve: {
      assetGeneralAdjustment: AssetGeneralAdjustmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetGeneralAdjustmentUpdateComponent,
    resolve: {
      assetGeneralAdjustment: AssetGeneralAdjustmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetGeneralAdjustmentUpdateComponent,
    resolve: {
      assetGeneralAdjustment: AssetGeneralAdjustmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetGeneralAdjustmentRoute)],
  exports: [RouterModule],
})
export class AssetGeneralAdjustmentRoutingModule {}
