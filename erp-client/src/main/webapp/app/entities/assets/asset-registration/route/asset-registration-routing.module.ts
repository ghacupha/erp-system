import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetRegistrationComponent } from '../list/asset-registration.component';
import { AssetRegistrationDetailComponent } from '../detail/asset-registration-detail.component';
import { AssetRegistrationUpdateComponent } from '../update/asset-registration-update.component';
import { AssetRegistrationRoutingResolveService } from './asset-registration-routing-resolve.service';

const assetRegistrationRoute: Routes = [
  {
    path: '',
    component: AssetRegistrationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetRegistrationDetailComponent,
    resolve: {
      assetRegistration: AssetRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetRegistrationUpdateComponent,
    resolve: {
      assetRegistration: AssetRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetRegistrationUpdateComponent,
    resolve: {
      assetRegistration: AssetRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetRegistrationRoute)],
  exports: [RouterModule],
})
export class AssetRegistrationRoutingModule {}
