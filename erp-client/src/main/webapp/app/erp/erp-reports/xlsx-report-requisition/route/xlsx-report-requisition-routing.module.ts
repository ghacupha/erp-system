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
import { XlsxReportRequisitionComponent } from '../list/xlsx-report-requisition.component';
import { XlsxReportRequisitionDetailComponent } from '../detail/xlsx-report-requisition-detail.component';
import { XlsxReportRequisitionUpdateComponent } from '../update/xlsx-report-requisition-update.component';
import { XlsxReportRequisitionRoutingResolveService } from './xlsx-report-requisition-routing-resolve.service';

const xlsxReportRequisitionRoute: Routes = [
  {
    path: '',
    component: XlsxReportRequisitionComponent,
    data: {
      defaultSort: 'id,desc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XlsxReportRequisitionDetailComponent,
    resolve: {
      xlsxReportRequisition: XlsxReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XlsxReportRequisitionUpdateComponent,
    resolve: {
      xlsxReportRequisition: XlsxReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XlsxReportRequisitionUpdateComponent,
    resolve: {
      xlsxReportRequisition: XlsxReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xlsxReportRequisitionRoute)],
  exports: [RouterModule],
})
export class XlsxReportRequisitionRoutingModule {}
