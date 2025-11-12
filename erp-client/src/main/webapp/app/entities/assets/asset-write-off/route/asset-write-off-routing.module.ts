///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { AssetWriteOffComponent } from '../list/asset-write-off.component';
import { AssetWriteOffDetailComponent } from '../detail/asset-write-off-detail.component';
import { AssetWriteOffUpdateComponent } from '../update/asset-write-off-update.component';
import { AssetWriteOffRoutingResolveService } from './asset-write-off-routing-resolve.service';

const assetWriteOffRoute: Routes = [
  {
    path: '',
    component: AssetWriteOffComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetWriteOffDetailComponent,
    resolve: {
      assetWriteOff: AssetWriteOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetWriteOffUpdateComponent,
    resolve: {
      assetWriteOff: AssetWriteOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetWriteOffUpdateComponent,
    resolve: {
      assetWriteOff: AssetWriteOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetWriteOffRoute)],
  exports: [RouterModule],
})
export class AssetWriteOffRoutingModule {}
