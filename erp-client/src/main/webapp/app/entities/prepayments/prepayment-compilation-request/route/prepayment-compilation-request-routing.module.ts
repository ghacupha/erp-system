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
import { PrepaymentCompilationRequestComponent } from '../list/prepayment-compilation-request.component';
import { PrepaymentCompilationRequestDetailComponent } from '../detail/prepayment-compilation-request-detail.component';
import { PrepaymentCompilationRequestUpdateComponent } from '../update/prepayment-compilation-request-update.component';
import { PrepaymentCompilationRequestRoutingResolveService } from './prepayment-compilation-request-routing-resolve.service';

const prepaymentCompilationRequestRoute: Routes = [
  {
    path: '',
    component: PrepaymentCompilationRequestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentCompilationRequestDetailComponent,
    resolve: {
      prepaymentCompilationRequest: PrepaymentCompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentCompilationRequestUpdateComponent,
    resolve: {
      prepaymentCompilationRequest: PrepaymentCompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentCompilationRequestUpdateComponent,
    resolve: {
      prepaymentCompilationRequest: PrepaymentCompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentCompilationRequestRoute)],
  exports: [RouterModule],
})
export class PrepaymentCompilationRequestRoutingModule {}
