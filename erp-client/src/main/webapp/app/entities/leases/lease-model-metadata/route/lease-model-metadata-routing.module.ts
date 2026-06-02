import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseModelMetadataComponent } from '../list/lease-model-metadata.component';
import { LeaseModelMetadataDetailComponent } from '../detail/lease-model-metadata-detail.component';
import { LeaseModelMetadataUpdateComponent } from '../update/lease-model-metadata-update.component';
import { LeaseModelMetadataRoutingResolveService } from './lease-model-metadata-routing-resolve.service';

const leaseModelMetadataRoute: Routes = [
  {
    path: '',
    component: LeaseModelMetadataComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseModelMetadataDetailComponent,
    resolve: {
      leaseModelMetadata: LeaseModelMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseModelMetadataUpdateComponent,
    resolve: {
      leaseModelMetadata: LeaseModelMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseModelMetadataUpdateComponent,
    resolve: {
      leaseModelMetadata: LeaseModelMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseModelMetadataRoute)],
  exports: [RouterModule],
})
export class LeaseModelMetadataRoutingModule {}
