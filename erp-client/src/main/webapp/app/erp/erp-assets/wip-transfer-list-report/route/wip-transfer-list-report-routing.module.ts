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
import { WIPTransferListReportComponent } from '../list/wip-transfer-list-report.component';
import { WIPTransferListReportDetailComponent } from '../detail/wip-transfer-list-report-detail.component';
import { WIPTransferListReportUpdateComponent } from '../update/wip-transfer-list-report-update.component';
import { WIPTransferListReportRoutingResolveService } from './wip-transfer-list-report-routing-resolve.service';

const wIPTransferListReportRoute: Routes = [
  {
    path: '',
    component: WIPTransferListReportComponent,
    data: {
      defaultSort: 'id,desc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WIPTransferListReportDetailComponent,
    resolve: {
      wIPTransferListReport: WIPTransferListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WIPTransferListReportUpdateComponent,
    resolve: {
      wIPTransferListReport: WIPTransferListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WIPTransferListReportUpdateComponent,
    resolve: {
      wIPTransferListReport: WIPTransferListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wIPTransferListReportRoute)],
  exports: [RouterModule],
})
export class WIPTransferListReportRoutingModule {}
