import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusinessDocumentComponent } from '../list/business-document.component';
import { BusinessDocumentDetailComponent } from '../detail/business-document-detail.component';
import { BusinessDocumentUpdateComponent } from '../update/business-document-update.component';
import { BusinessDocumentRoutingResolveService } from './business-document-routing-resolve.service';

const businessDocumentRoute: Routes = [
  {
    path: '',
    component: BusinessDocumentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessDocumentDetailComponent,
    resolve: {
      businessDocument: BusinessDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessDocumentUpdateComponent,
    resolve: {
      businessDocument: BusinessDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessDocumentUpdateComponent,
    resolve: {
      businessDocument: BusinessDocumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(businessDocumentRoute)],
  exports: [RouterModule],
})
export class BusinessDocumentRoutingModule {}
