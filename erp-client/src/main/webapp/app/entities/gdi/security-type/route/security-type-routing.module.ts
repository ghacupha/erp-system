import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SecurityTypeComponent } from '../list/security-type.component';
import { SecurityTypeDetailComponent } from '../detail/security-type-detail.component';
import { SecurityTypeUpdateComponent } from '../update/security-type-update.component';
import { SecurityTypeRoutingResolveService } from './security-type-routing-resolve.service';

const securityTypeRoute: Routes = [
  {
    path: '',
    component: SecurityTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SecurityTypeDetailComponent,
    resolve: {
      securityType: SecurityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SecurityTypeUpdateComponent,
    resolve: {
      securityType: SecurityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SecurityTypeUpdateComponent,
    resolve: {
      securityType: SecurityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(securityTypeRoute)],
  exports: [RouterModule],
})
export class SecurityTypeRoutingModule {}
