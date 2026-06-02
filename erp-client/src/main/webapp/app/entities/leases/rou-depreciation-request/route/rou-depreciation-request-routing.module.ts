import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouDepreciationRequestComponent } from '../list/rou-depreciation-request.component';
import { RouDepreciationRequestDetailComponent } from '../detail/rou-depreciation-request-detail.component';
import { RouDepreciationRequestUpdateComponent } from '../update/rou-depreciation-request-update.component';
import { RouDepreciationRequestRoutingResolveService } from './rou-depreciation-request-routing-resolve.service';

const rouDepreciationRequestRoute: Routes = [
  {
    path: '',
    component: RouDepreciationRequestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouDepreciationRequestDetailComponent,
    resolve: {
      rouDepreciationRequest: RouDepreciationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouDepreciationRequestUpdateComponent,
    resolve: {
      rouDepreciationRequest: RouDepreciationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouDepreciationRequestUpdateComponent,
    resolve: {
      rouDepreciationRequest: RouDepreciationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouDepreciationRequestRoute)],
  exports: [RouterModule],
})
export class RouDepreciationRequestRoutingModule {}
