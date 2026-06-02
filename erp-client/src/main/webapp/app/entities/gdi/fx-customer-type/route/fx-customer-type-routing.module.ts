import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FxCustomerTypeComponent } from '../list/fx-customer-type.component';
import { FxCustomerTypeDetailComponent } from '../detail/fx-customer-type-detail.component';
import { FxCustomerTypeUpdateComponent } from '../update/fx-customer-type-update.component';
import { FxCustomerTypeRoutingResolveService } from './fx-customer-type-routing-resolve.service';

const fxCustomerTypeRoute: Routes = [
  {
    path: '',
    component: FxCustomerTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FxCustomerTypeDetailComponent,
    resolve: {
      fxCustomerType: FxCustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FxCustomerTypeUpdateComponent,
    resolve: {
      fxCustomerType: FxCustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FxCustomerTypeUpdateComponent,
    resolve: {
      fxCustomerType: FxCustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fxCustomerTypeRoute)],
  exports: [RouterModule],
})
export class FxCustomerTypeRoutingModule {}
