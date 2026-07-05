import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DerivativeUnderlyingAssetComponent } from '../list/derivative-underlying-asset.component';
import { DerivativeUnderlyingAssetDetailComponent } from '../detail/derivative-underlying-asset-detail.component';
import { DerivativeUnderlyingAssetUpdateComponent } from '../update/derivative-underlying-asset-update.component';
import { DerivativeUnderlyingAssetRoutingResolveService } from './derivative-underlying-asset-routing-resolve.service';

const derivativeUnderlyingAssetRoute: Routes = [
  {
    path: '',
    component: DerivativeUnderlyingAssetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DerivativeUnderlyingAssetDetailComponent,
    resolve: {
      derivativeUnderlyingAsset: DerivativeUnderlyingAssetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DerivativeUnderlyingAssetUpdateComponent,
    resolve: {
      derivativeUnderlyingAsset: DerivativeUnderlyingAssetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DerivativeUnderlyingAssetUpdateComponent,
    resolve: {
      derivativeUnderlyingAsset: DerivativeUnderlyingAssetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(derivativeUnderlyingAssetRoute)],
  exports: [RouterModule],
})
export class DerivativeUnderlyingAssetRoutingModule {}
