import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FixedAssetAcquisitionComponent } from '../list/fixed-asset-acquisition.component';
import { FixedAssetAcquisitionDetailComponent } from '../detail/fixed-asset-acquisition-detail.component';
import { FixedAssetAcquisitionUpdateComponent } from '../update/fixed-asset-acquisition-update.component';
import { FixedAssetAcquisitionRoutingResolveService } from './fixed-asset-acquisition-routing-resolve.service';

const fixedAssetAcquisitionRoute: Routes = [
  {
    path: '',
    component: FixedAssetAcquisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FixedAssetAcquisitionDetailComponent,
    resolve: {
      fixedAssetAcquisition: FixedAssetAcquisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FixedAssetAcquisitionUpdateComponent,
    resolve: {
      fixedAssetAcquisition: FixedAssetAcquisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FixedAssetAcquisitionUpdateComponent,
    resolve: {
      fixedAssetAcquisition: FixedAssetAcquisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fixedAssetAcquisitionRoute)],
  exports: [RouterModule],
})
export class FixedAssetAcquisitionRoutingModule {}
