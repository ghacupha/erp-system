import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CollateralInformationComponent } from '../list/collateral-information.component';
import { CollateralInformationDetailComponent } from '../detail/collateral-information-detail.component';
import { CollateralInformationUpdateComponent } from '../update/collateral-information-update.component';
import { CollateralInformationRoutingResolveService } from './collateral-information-routing-resolve.service';

const collateralInformationRoute: Routes = [
  {
    path: '',
    component: CollateralInformationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CollateralInformationDetailComponent,
    resolve: {
      collateralInformation: CollateralInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CollateralInformationUpdateComponent,
    resolve: {
      collateralInformation: CollateralInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CollateralInformationUpdateComponent,
    resolve: {
      collateralInformation: CollateralInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(collateralInformationRoute)],
  exports: [RouterModule],
})
export class CollateralInformationRoutingModule {}
