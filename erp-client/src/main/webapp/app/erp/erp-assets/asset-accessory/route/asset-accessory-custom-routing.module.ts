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
import { AssetAccessoryUpdateComponent } from '../update/asset-accessory-update.component';
import { AssetAccessoryComponent } from '../list/asset-accessory.component';
import { AssetAccessoryDetailComponent } from '../detail/asset-accessory-detail.component';
import { AssetAccessoryRoutingResolveService } from './asset-accessory-routing-resolve.service';

const assetAccessoryRoute: Routes = [
  {
    path: '',
    component: AssetAccessoryComponent,
    data: {
      defaultSort: 'id,desc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetAccessoryDetailComponent,
    resolve: {
      assetAccessory: AssetAccessoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetAccessoryUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/copy',
    component: AssetAccessoryUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetAccessoryUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetAccessoryRoute)],
  exports: [RouterModule],
})
export class AssetAccessoryCustomRoutingModule {}
