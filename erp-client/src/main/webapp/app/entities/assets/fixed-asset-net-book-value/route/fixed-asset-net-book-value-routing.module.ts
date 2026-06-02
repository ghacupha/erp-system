import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FixedAssetNetBookValueComponent } from '../list/fixed-asset-net-book-value.component';
import { FixedAssetNetBookValueDetailComponent } from '../detail/fixed-asset-net-book-value-detail.component';
import { FixedAssetNetBookValueUpdateComponent } from '../update/fixed-asset-net-book-value-update.component';
import { FixedAssetNetBookValueRoutingResolveService } from './fixed-asset-net-book-value-routing-resolve.service';

const fixedAssetNetBookValueRoute: Routes = [
  {
    path: '',
    component: FixedAssetNetBookValueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FixedAssetNetBookValueDetailComponent,
    resolve: {
      fixedAssetNetBookValue: FixedAssetNetBookValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FixedAssetNetBookValueUpdateComponent,
    resolve: {
      fixedAssetNetBookValue: FixedAssetNetBookValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FixedAssetNetBookValueUpdateComponent,
    resolve: {
      fixedAssetNetBookValue: FixedAssetNetBookValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fixedAssetNetBookValueRoute)],
  exports: [RouterModule],
})
export class FixedAssetNetBookValueRoutingModule {}
