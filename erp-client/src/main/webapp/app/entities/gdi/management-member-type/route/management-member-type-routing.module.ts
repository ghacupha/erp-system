import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManagementMemberTypeComponent } from '../list/management-member-type.component';
import { ManagementMemberTypeDetailComponent } from '../detail/management-member-type-detail.component';
import { ManagementMemberTypeUpdateComponent } from '../update/management-member-type-update.component';
import { ManagementMemberTypeRoutingResolveService } from './management-member-type-routing-resolve.service';

const managementMemberTypeRoute: Routes = [
  {
    path: '',
    component: ManagementMemberTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManagementMemberTypeDetailComponent,
    resolve: {
      managementMemberType: ManagementMemberTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManagementMemberTypeUpdateComponent,
    resolve: {
      managementMemberType: ManagementMemberTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManagementMemberTypeUpdateComponent,
    resolve: {
      managementMemberType: ManagementMemberTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(managementMemberTypeRoute)],
  exports: [RouterModule],
})
export class ManagementMemberTypeRoutingModule {}
