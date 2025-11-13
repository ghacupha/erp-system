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
import { LoanProductTypeComponent } from '../list/loan-product-type.component';
import { LoanProductTypeDetailComponent } from '../detail/loan-product-type-detail.component';
import { LoanProductTypeUpdateComponent } from '../update/loan-product-type-update.component';
import { LoanProductTypeRoutingResolveService } from './loan-product-type-routing-resolve.service';

const loanProductTypeRoute: Routes = [
  {
    path: '',
    component: LoanProductTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanProductTypeDetailComponent,
    resolve: {
      loanProductType: LoanProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanProductTypeUpdateComponent,
    resolve: {
      loanProductType: LoanProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanProductTypeUpdateComponent,
    resolve: {
      loanProductType: LoanProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanProductTypeRoute)],
  exports: [RouterModule],
})
export class LoanProductTypeRoutingModule {}
