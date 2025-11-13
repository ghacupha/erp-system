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
import { LoanDeclineReasonComponent } from '../list/loan-decline-reason.component';
import { LoanDeclineReasonDetailComponent } from '../detail/loan-decline-reason-detail.component';
import { LoanDeclineReasonUpdateComponent } from '../update/loan-decline-reason-update.component';
import { LoanDeclineReasonRoutingResolveService } from './loan-decline-reason-routing-resolve.service';

const loanDeclineReasonRoute: Routes = [
  {
    path: '',
    component: LoanDeclineReasonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanDeclineReasonDetailComponent,
    resolve: {
      loanDeclineReason: LoanDeclineReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanDeclineReasonUpdateComponent,
    resolve: {
      loanDeclineReason: LoanDeclineReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanDeclineReasonUpdateComponent,
    resolve: {
      loanDeclineReason: LoanDeclineReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanDeclineReasonRoute)],
  exports: [RouterModule],
})
export class LoanDeclineReasonRoutingModule {}
