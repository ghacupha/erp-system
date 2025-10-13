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
import { CrbReportRequestReasonsComponent } from '../list/crb-report-request-reasons.component';
import { CrbReportRequestReasonsDetailComponent } from '../detail/crb-report-request-reasons-detail.component';
import { CrbReportRequestReasonsUpdateComponent } from '../update/crb-report-request-reasons-update.component';
import { CrbReportRequestReasonsRoutingResolveService } from './crb-report-request-reasons-routing-resolve.service';

const crbReportRequestReasonsRoute: Routes = [
  {
    path: '',
    component: CrbReportRequestReasonsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbReportRequestReasonsDetailComponent,
    resolve: {
      crbReportRequestReasons: CrbReportRequestReasonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbReportRequestReasonsUpdateComponent,
    resolve: {
      crbReportRequestReasons: CrbReportRequestReasonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbReportRequestReasonsUpdateComponent,
    resolve: {
      crbReportRequestReasons: CrbReportRequestReasonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbReportRequestReasonsRoute)],
  exports: [RouterModule],
})
export class CrbReportRequestReasonsRoutingModule {}
