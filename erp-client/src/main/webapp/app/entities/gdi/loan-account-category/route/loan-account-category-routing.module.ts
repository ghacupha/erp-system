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
import { LoanAccountCategoryComponent } from '../list/loan-account-category.component';
import { LoanAccountCategoryDetailComponent } from '../detail/loan-account-category-detail.component';
import { LoanAccountCategoryUpdateComponent } from '../update/loan-account-category-update.component';
import { LoanAccountCategoryRoutingResolveService } from './loan-account-category-routing-resolve.service';

const loanAccountCategoryRoute: Routes = [
  {
    path: '',
    component: LoanAccountCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanAccountCategoryDetailComponent,
    resolve: {
      loanAccountCategory: LoanAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanAccountCategoryUpdateComponent,
    resolve: {
      loanAccountCategory: LoanAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanAccountCategoryUpdateComponent,
    resolve: {
      loanAccountCategory: LoanAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanAccountCategoryRoute)],
  exports: [RouterModule],
})
export class LoanAccountCategoryRoutingModule {}
