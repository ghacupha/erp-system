import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerComplaintStatusTypeComponent } from '../list/customer-complaint-status-type.component';
import { CustomerComplaintStatusTypeDetailComponent } from '../detail/customer-complaint-status-type-detail.component';
import { CustomerComplaintStatusTypeUpdateComponent } from '../update/customer-complaint-status-type-update.component';
import { CustomerComplaintStatusTypeRoutingResolveService } from './customer-complaint-status-type-routing-resolve.service';

const customerComplaintStatusTypeRoute: Routes = [
  {
    path: '',
    component: CustomerComplaintStatusTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerComplaintStatusTypeDetailComponent,
    resolve: {
      customerComplaintStatusType: CustomerComplaintStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerComplaintStatusTypeUpdateComponent,
    resolve: {
      customerComplaintStatusType: CustomerComplaintStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerComplaintStatusTypeUpdateComponent,
    resolve: {
      customerComplaintStatusType: CustomerComplaintStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerComplaintStatusTypeRoute)],
  exports: [RouterModule],
})
export class CustomerComplaintStatusTypeRoutingModule {}
