import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetRevaluationComponent } from '../list/asset-revaluation.component';
import { AssetRevaluationDetailComponent } from '../detail/asset-revaluation-detail.component';
import { AssetRevaluationUpdateComponent } from '../update/asset-revaluation-update.component';
import { AssetRevaluationRoutingResolveService } from './asset-revaluation-routing-resolve.service';

const assetRevaluationRoute: Routes = [
  {
    path: '',
    component: AssetRevaluationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetRevaluationDetailComponent,
    resolve: {
      assetRevaluation: AssetRevaluationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetRevaluationUpdateComponent,
    resolve: {
      assetRevaluation: AssetRevaluationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetRevaluationUpdateComponent,
    resolve: {
      assetRevaluation: AssetRevaluationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetRevaluationRoute)],
  exports: [RouterModule],
})
export class AssetRevaluationRoutingModule {}
