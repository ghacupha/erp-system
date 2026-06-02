import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SecurityClearanceComponent } from '../list/security-clearance.component';
import { SecurityClearanceDetailComponent } from '../detail/security-clearance-detail.component';
import { SecurityClearanceUpdateComponent } from '../update/security-clearance-update.component';
import { SecurityClearanceRoutingResolveService } from './security-clearance-routing-resolve.service';

const securityClearanceRoute: Routes = [
  {
    path: '',
    component: SecurityClearanceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SecurityClearanceDetailComponent,
    resolve: {
      securityClearance: SecurityClearanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SecurityClearanceUpdateComponent,
    resolve: {
      securityClearance: SecurityClearanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SecurityClearanceUpdateComponent,
    resolve: {
      securityClearance: SecurityClearanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(securityClearanceRoute)],
  exports: [RouterModule],
})
export class SecurityClearanceRoutingModule {}
