import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DerivativeSubTypeComponent } from '../list/derivative-sub-type.component';
import { DerivativeSubTypeDetailComponent } from '../detail/derivative-sub-type-detail.component';
import { DerivativeSubTypeUpdateComponent } from '../update/derivative-sub-type-update.component';
import { DerivativeSubTypeRoutingResolveService } from './derivative-sub-type-routing-resolve.service';

const derivativeSubTypeRoute: Routes = [
  {
    path: '',
    component: DerivativeSubTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DerivativeSubTypeDetailComponent,
    resolve: {
      derivativeSubType: DerivativeSubTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DerivativeSubTypeUpdateComponent,
    resolve: {
      derivativeSubType: DerivativeSubTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DerivativeSubTypeUpdateComponent,
    resolve: {
      derivativeSubType: DerivativeSubTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(derivativeSubTypeRoute)],
  exports: [RouterModule],
})
export class DerivativeSubTypeRoutingModule {}
