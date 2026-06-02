import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FxRateTypeComponent } from '../list/fx-rate-type.component';
import { FxRateTypeDetailComponent } from '../detail/fx-rate-type-detail.component';
import { FxRateTypeUpdateComponent } from '../update/fx-rate-type-update.component';
import { FxRateTypeRoutingResolveService } from './fx-rate-type-routing-resolve.service';

const fxRateTypeRoute: Routes = [
  {
    path: '',
    component: FxRateTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FxRateTypeDetailComponent,
    resolve: {
      fxRateType: FxRateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FxRateTypeUpdateComponent,
    resolve: {
      fxRateType: FxRateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FxRateTypeUpdateComponent,
    resolve: {
      fxRateType: FxRateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fxRateTypeRoute)],
  exports: [RouterModule],
})
export class FxRateTypeRoutingModule {}
