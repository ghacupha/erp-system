import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CollateralTypeComponent } from '../list/collateral-type.component';
import { CollateralTypeDetailComponent } from '../detail/collateral-type-detail.component';
import { CollateralTypeUpdateComponent } from '../update/collateral-type-update.component';
import { CollateralTypeRoutingResolveService } from './collateral-type-routing-resolve.service';

const collateralTypeRoute: Routes = [
  {
    path: '',
    component: CollateralTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CollateralTypeDetailComponent,
    resolve: {
      collateralType: CollateralTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CollateralTypeUpdateComponent,
    resolve: {
      collateralType: CollateralTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CollateralTypeUpdateComponent,
    resolve: {
      collateralType: CollateralTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(collateralTypeRoute)],
  exports: [RouterModule],
})
export class CollateralTypeRoutingModule {}
