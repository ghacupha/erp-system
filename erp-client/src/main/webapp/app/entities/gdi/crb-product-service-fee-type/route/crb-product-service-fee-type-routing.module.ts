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
import { CrbProductServiceFeeTypeComponent } from '../list/crb-product-service-fee-type.component';
import { CrbProductServiceFeeTypeDetailComponent } from '../detail/crb-product-service-fee-type-detail.component';
import { CrbProductServiceFeeTypeUpdateComponent } from '../update/crb-product-service-fee-type-update.component';
import { CrbProductServiceFeeTypeRoutingResolveService } from './crb-product-service-fee-type-routing-resolve.service';

const crbProductServiceFeeTypeRoute: Routes = [
  {
    path: '',
    component: CrbProductServiceFeeTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbProductServiceFeeTypeDetailComponent,
    resolve: {
      crbProductServiceFeeType: CrbProductServiceFeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbProductServiceFeeTypeUpdateComponent,
    resolve: {
      crbProductServiceFeeType: CrbProductServiceFeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbProductServiceFeeTypeUpdateComponent,
    resolve: {
      crbProductServiceFeeType: CrbProductServiceFeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbProductServiceFeeTypeRoute)],
  exports: [RouterModule],
})
export class CrbProductServiceFeeTypeRoutingModule {}
