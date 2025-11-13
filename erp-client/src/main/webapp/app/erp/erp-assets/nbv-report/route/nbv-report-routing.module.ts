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
import { NbvReportComponent } from '../list/nbv-report.component';
import { NbvReportDetailComponent } from '../detail/nbv-report-detail.component';
import { NbvReportUpdateComponent } from '../update/nbv-report-update.component';
import { NbvReportRoutingResolveService } from './nbv-report-routing-resolve.service';

const nbvReportRoute: Routes = [
  {
    path: '',
    component: NbvReportComponent,
    data: {
      defaultSort: 'id,desc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NbvReportDetailComponent,
    resolve: {
      nbvReport: NbvReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NbvReportUpdateComponent,
    resolve: {
      nbvReport: NbvReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NbvReportUpdateComponent,
    resolve: {
      nbvReport: NbvReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nbvReportRoute)],
  exports: [RouterModule],
})
export class NbvReportRoutingModule {}
