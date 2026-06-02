import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetDisposalComponent } from '../list/asset-disposal.component';
import { AssetDisposalDetailComponent } from '../detail/asset-disposal-detail.component';
import { AssetDisposalUpdateComponent } from '../update/asset-disposal-update.component';
import { AssetDisposalRoutingResolveService } from './asset-disposal-routing-resolve.service';

const assetDisposalRoute: Routes = [
  {
    path: '',
    component: AssetDisposalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetDisposalDetailComponent,
    resolve: {
      assetDisposal: AssetDisposalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetDisposalUpdateComponent,
    resolve: {
      assetDisposal: AssetDisposalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetDisposalUpdateComponent,
    resolve: {
      assetDisposal: AssetDisposalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetDisposalRoute)],
  exports: [RouterModule],
})
export class AssetDisposalRoutingModule {}
