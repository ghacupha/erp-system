import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StaffRoleTypeComponent } from '../list/staff-role-type.component';
import { StaffRoleTypeDetailComponent } from '../detail/staff-role-type-detail.component';
import { StaffRoleTypeUpdateComponent } from '../update/staff-role-type-update.component';
import { StaffRoleTypeRoutingResolveService } from './staff-role-type-routing-resolve.service';

const staffRoleTypeRoute: Routes = [
  {
    path: '',
    component: StaffRoleTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StaffRoleTypeDetailComponent,
    resolve: {
      staffRoleType: StaffRoleTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StaffRoleTypeUpdateComponent,
    resolve: {
      staffRoleType: StaffRoleTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StaffRoleTypeUpdateComponent,
    resolve: {
      staffRoleType: StaffRoleTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(staffRoleTypeRoute)],
  exports: [RouterModule],
})
export class StaffRoleTypeRoutingModule {}
