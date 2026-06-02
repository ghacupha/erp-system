import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemContentTypeComponent } from '../list/system-content-type.component';
import { SystemContentTypeDetailComponent } from '../detail/system-content-type-detail.component';
import { SystemContentTypeUpdateComponent } from '../update/system-content-type-update.component';
import { SystemContentTypeRoutingResolveService } from './system-content-type-routing-resolve.service';

const systemContentTypeRoute: Routes = [
  {
    path: '',
    component: SystemContentTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemContentTypeDetailComponent,
    resolve: {
      systemContentType: SystemContentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemContentTypeUpdateComponent,
    resolve: {
      systemContentType: SystemContentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemContentTypeUpdateComponent,
    resolve: {
      systemContentType: SystemContentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemContentTypeRoute)],
  exports: [RouterModule],
})
export class SystemContentTypeRoutingModule {}
