///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkProjectRegisterComponent } from '../list/work-project-register.component';
import { WorkProjectRegisterDetailComponent } from '../detail/work-project-register-detail.component';
import { WorkProjectRegisterUpdateComponent } from '../update/work-project-register-update.component';
import { WorkProjectRegisterRoutingResolveService } from './work-project-register-routing-resolve.service';

const workProjectRegisterRoute: Routes = [
  {
    path: '',
    component: WorkProjectRegisterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkProjectRegisterDetailComponent,
    resolve: {
      workProjectRegister: WorkProjectRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkProjectRegisterUpdateComponent,
    resolve: {
      workProjectRegister: WorkProjectRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkProjectRegisterUpdateComponent,
    resolve: {
      workProjectRegister: WorkProjectRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workProjectRegisterRoute)],
  exports: [RouterModule],
})
export class WorkProjectRegisterRoutingModule {}
