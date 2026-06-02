import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UniversallyUniqueMappingComponent } from '../list/universally-unique-mapping.component';
import { UniversallyUniqueMappingDetailComponent } from '../detail/universally-unique-mapping-detail.component';
import { UniversallyUniqueMappingUpdateComponent } from '../update/universally-unique-mapping-update.component';
import { UniversallyUniqueMappingRoutingResolveService } from './universally-unique-mapping-routing-resolve.service';

const universallyUniqueMappingRoute: Routes = [
  {
    path: '',
    component: UniversallyUniqueMappingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UniversallyUniqueMappingDetailComponent,
    resolve: {
      universallyUniqueMapping: UniversallyUniqueMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UniversallyUniqueMappingUpdateComponent,
    resolve: {
      universallyUniqueMapping: UniversallyUniqueMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UniversallyUniqueMappingUpdateComponent,
    resolve: {
      universallyUniqueMapping: UniversallyUniqueMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(universallyUniqueMappingRoute)],
  exports: [RouterModule],
})
export class UniversallyUniqueMappingRoutingModule {}
