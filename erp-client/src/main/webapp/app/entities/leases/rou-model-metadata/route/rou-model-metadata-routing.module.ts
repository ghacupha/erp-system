import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouModelMetadataComponent } from '../list/rou-model-metadata.component';
import { RouModelMetadataDetailComponent } from '../detail/rou-model-metadata-detail.component';
import { RouModelMetadataUpdateComponent } from '../update/rou-model-metadata-update.component';
import { RouModelMetadataRoutingResolveService } from './rou-model-metadata-routing-resolve.service';

const rouModelMetadataRoute: Routes = [
  {
    path: '',
    component: RouModelMetadataComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouModelMetadataDetailComponent,
    resolve: {
      rouModelMetadata: RouModelMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouModelMetadataUpdateComponent,
    resolve: {
      rouModelMetadata: RouModelMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouModelMetadataUpdateComponent,
    resolve: {
      rouModelMetadata: RouModelMetadataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouModelMetadataRoute)],
  exports: [RouterModule],
})
export class RouModelMetadataRoutingModule {}
