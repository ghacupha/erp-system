import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepartmentTypeComponent } from '../list/department-type.component';
import { DepartmentTypeDetailComponent } from '../detail/department-type-detail.component';
import { DepartmentTypeUpdateComponent } from '../update/department-type-update.component';
import { DepartmentTypeRoutingResolveService } from './department-type-routing-resolve.service';

const departmentTypeRoute: Routes = [
  {
    path: '',
    component: DepartmentTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepartmentTypeDetailComponent,
    resolve: {
      departmentType: DepartmentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepartmentTypeUpdateComponent,
    resolve: {
      departmentType: DepartmentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepartmentTypeUpdateComponent,
    resolve: {
      departmentType: DepartmentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(departmentTypeRoute)],
  exports: [RouterModule],
})
export class DepartmentTypeRoutingModule {}
