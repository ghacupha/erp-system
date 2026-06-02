import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NatureOfCustomerComplaintsComponent } from '../list/nature-of-customer-complaints.component';
import { NatureOfCustomerComplaintsDetailComponent } from '../detail/nature-of-customer-complaints-detail.component';
import { NatureOfCustomerComplaintsUpdateComponent } from '../update/nature-of-customer-complaints-update.component';
import { NatureOfCustomerComplaintsRoutingResolveService } from './nature-of-customer-complaints-routing-resolve.service';

const natureOfCustomerComplaintsRoute: Routes = [
  {
    path: '',
    component: NatureOfCustomerComplaintsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NatureOfCustomerComplaintsDetailComponent,
    resolve: {
      natureOfCustomerComplaints: NatureOfCustomerComplaintsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NatureOfCustomerComplaintsUpdateComponent,
    resolve: {
      natureOfCustomerComplaints: NatureOfCustomerComplaintsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NatureOfCustomerComplaintsUpdateComponent,
    resolve: {
      natureOfCustomerComplaints: NatureOfCustomerComplaintsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(natureOfCustomerComplaintsRoute)],
  exports: [RouterModule],
})
export class NatureOfCustomerComplaintsRoutingModule {}
