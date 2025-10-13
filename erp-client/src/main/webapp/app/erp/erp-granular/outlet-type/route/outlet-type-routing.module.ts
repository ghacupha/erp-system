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
import { OutletTypeComponent } from '../list/outlet-type.component';
import { OutletTypeDetailComponent } from '../detail/outlet-type-detail.component';
import { OutletTypeUpdateComponent } from '../update/outlet-type-update.component';
import { OutletTypeRoutingResolveService } from './outlet-type-routing-resolve.service';

const outletTypeRoute: Routes = [
  {
    path: '',
    component: OutletTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OutletTypeDetailComponent,
    resolve: {
      outletType: OutletTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OutletTypeUpdateComponent,
    resolve: {
      outletType: OutletTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OutletTypeUpdateComponent,
    resolve: {
      outletType: OutletTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(outletTypeRoute)],
  exports: [RouterModule],
})
export class OutletTypeRoutingModule {}
