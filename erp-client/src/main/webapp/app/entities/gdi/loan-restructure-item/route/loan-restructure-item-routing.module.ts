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
import { LoanRestructureItemComponent } from '../list/loan-restructure-item.component';
import { LoanRestructureItemDetailComponent } from '../detail/loan-restructure-item-detail.component';
import { LoanRestructureItemUpdateComponent } from '../update/loan-restructure-item-update.component';
import { LoanRestructureItemRoutingResolveService } from './loan-restructure-item-routing-resolve.service';

const loanRestructureItemRoute: Routes = [
  {
    path: '',
    component: LoanRestructureItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanRestructureItemDetailComponent,
    resolve: {
      loanRestructureItem: LoanRestructureItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanRestructureItemUpdateComponent,
    resolve: {
      loanRestructureItem: LoanRestructureItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanRestructureItemUpdateComponent,
    resolve: {
      loanRestructureItem: LoanRestructureItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanRestructureItemRoute)],
  exports: [RouterModule],
})
export class LoanRestructureItemRoutingModule {}
