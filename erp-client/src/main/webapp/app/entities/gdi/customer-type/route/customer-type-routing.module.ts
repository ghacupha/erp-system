import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerTypeComponent } from '../list/customer-type.component';
import { CustomerTypeDetailComponent } from '../detail/customer-type-detail.component';
import { CustomerTypeUpdateComponent } from '../update/customer-type-update.component';
import { CustomerTypeRoutingResolveService } from './customer-type-routing-resolve.service';

const customerTypeRoute: Routes = [
  {
    path: '',
    component: CustomerTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerTypeDetailComponent,
    resolve: {
      customerType: CustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerTypeUpdateComponent,
    resolve: {
      customerType: CustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerTypeUpdateComponent,
    resolve: {
      customerType: CustomerTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerTypeRoute)],
  exports: [RouterModule],
})
export class CustomerTypeRoutingModule {}
