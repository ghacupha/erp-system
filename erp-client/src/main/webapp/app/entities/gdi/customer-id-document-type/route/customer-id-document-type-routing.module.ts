import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerIDDocumentTypeComponent } from '../list/customer-id-document-type.component';
import { CustomerIDDocumentTypeDetailComponent } from '../detail/customer-id-document-type-detail.component';
import { CustomerIDDocumentTypeUpdateComponent } from '../update/customer-id-document-type-update.component';
import { CustomerIDDocumentTypeRoutingResolveService } from './customer-id-document-type-routing-resolve.service';

const customerIDDocumentTypeRoute: Routes = [
  {
    path: '',
    component: CustomerIDDocumentTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerIDDocumentTypeDetailComponent,
    resolve: {
      customerIDDocumentType: CustomerIDDocumentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerIDDocumentTypeUpdateComponent,
    resolve: {
      customerIDDocumentType: CustomerIDDocumentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerIDDocumentTypeUpdateComponent,
    resolve: {
      customerIDDocumentType: CustomerIDDocumentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerIDDocumentTypeRoute)],
  exports: [RouterModule],
})
export class CustomerIDDocumentTypeRoutingModule {}
