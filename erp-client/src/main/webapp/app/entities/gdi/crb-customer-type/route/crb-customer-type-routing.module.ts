import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbCustomerTypeComponent } from '../list/crb-customer-type.component';
import { CrbCustomerTypeDetailComponent } from '../detail/crb-customer-type-detail.component';
import { CrbCustomerTypeUpdateComponent } from '../update/crb-customer-type-update.component';
import { CrbCustomerTypeRoutingResolveService } from './crb-customer-type-routing-resolve.service';

const crbCustomerTypeRoute: Routes = [
  {
    path: '',
    component: CrbCustomerTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbCustomerTypeDetailComponent,
    resolve: {
      crbCustomerType: CrbCustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbCustomerTypeUpdateComponent,
    resolve: {
      crbCustomerType: CrbCustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbCustomerTypeUpdateComponent,
    resolve: {
      crbCustomerType: CrbCustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbCustomerTypeRoute)],
  exports: [RouterModule],
})
export class CrbCustomerTypeRoutingModule {}
