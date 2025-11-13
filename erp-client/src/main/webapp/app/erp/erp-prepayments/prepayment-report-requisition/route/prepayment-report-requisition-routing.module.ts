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
import { PrepaymentReportRequisitionComponent } from '../list/prepayment-report-requisition.component';
import { PrepaymentReportRequisitionDetailComponent } from '../detail/prepayment-report-requisition-detail.component';
import { PrepaymentReportRequisitionUpdateComponent } from '../update/prepayment-report-requisition-update.component';
import { PrepaymentReportRequisitionRoutingResolveService } from './prepayment-report-requisition-routing-resolve.service';

const prepaymentReportRequisitionRoute: Routes = [
  {
    path: '',
    component: PrepaymentReportRequisitionComponent,
    data: {
      defaultSort: 'id,desc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentReportRequisitionDetailComponent,
    resolve: {
      prepaymentReportRequisition: PrepaymentReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentReportRequisitionUpdateComponent,
    resolve: {
      prepaymentReportRequisition: PrepaymentReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentReportRequisitionUpdateComponent,
    resolve: {
      prepaymentReportRequisition: PrepaymentReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentReportRequisitionRoute)],
  exports: [RouterModule],
})
export class PrepaymentReportRequisitionRoutingModule {}
