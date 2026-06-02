import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusinessStampComponent } from '../list/business-stamp.component';
import { BusinessStampDetailComponent } from '../detail/business-stamp-detail.component';
import { BusinessStampUpdateComponent } from '../update/business-stamp-update.component';
import { BusinessStampRoutingResolveService } from './business-stamp-routing-resolve.service';

const businessStampRoute: Routes = [
  {
    path: '',
    component: BusinessStampComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessStampDetailComponent,
    resolve: {
      businessStamp: BusinessStampRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessStampUpdateComponent,
    resolve: {
      businessStamp: BusinessStampRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessStampUpdateComponent,
    resolve: {
      businessStamp: BusinessStampRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(businessStampRoute)],
  exports: [RouterModule],
})
export class BusinessStampRoutingModule {}
