import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GlMappingComponent } from '../list/gl-mapping.component';
import { GlMappingDetailComponent } from '../detail/gl-mapping-detail.component';
import { GlMappingUpdateComponent } from '../update/gl-mapping-update.component';
import { GlMappingRoutingResolveService } from './gl-mapping-routing-resolve.service';

const glMappingRoute: Routes = [
  {
    path: '',
    component: GlMappingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GlMappingDetailComponent,
    resolve: {
      glMapping: GlMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GlMappingUpdateComponent,
    resolve: {
      glMapping: GlMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GlMappingUpdateComponent,
    resolve: {
      glMapping: GlMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(glMappingRoute)],
  exports: [RouterModule],
})
export class GlMappingRoutingModule {}
