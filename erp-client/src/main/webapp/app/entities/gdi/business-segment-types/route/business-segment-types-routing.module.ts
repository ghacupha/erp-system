import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusinessSegmentTypesComponent } from '../list/business-segment-types.component';
import { BusinessSegmentTypesDetailComponent } from '../detail/business-segment-types-detail.component';
import { BusinessSegmentTypesUpdateComponent } from '../update/business-segment-types-update.component';
import { BusinessSegmentTypesRoutingResolveService } from './business-segment-types-routing-resolve.service';

const businessSegmentTypesRoute: Routes = [
  {
    path: '',
    component: BusinessSegmentTypesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessSegmentTypesDetailComponent,
    resolve: {
      businessSegmentTypes: BusinessSegmentTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessSegmentTypesUpdateComponent,
    resolve: {
      businessSegmentTypes: BusinessSegmentTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessSegmentTypesUpdateComponent,
    resolve: {
      businessSegmentTypes: BusinessSegmentTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(businessSegmentTypesRoute)],
  exports: [RouterModule],
})
export class BusinessSegmentTypesRoutingModule {}
