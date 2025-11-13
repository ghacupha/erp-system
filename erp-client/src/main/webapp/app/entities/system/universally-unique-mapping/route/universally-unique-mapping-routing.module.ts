///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
