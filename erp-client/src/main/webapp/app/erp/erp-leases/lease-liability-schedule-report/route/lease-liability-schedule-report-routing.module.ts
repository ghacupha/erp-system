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
import { LeaseLiabilityScheduleReportComponent } from '../list/lease-liability-schedule-report.component';
import { LeaseLiabilityScheduleReportDetailComponent } from '../detail/lease-liability-schedule-report-detail.component';
import { LeaseLiabilityScheduleReportUpdateComponent } from '../update/lease-liability-schedule-report-update.component';
import { LeaseLiabilityScheduleReportRoutingResolveService } from './lease-liability-schedule-report-routing-resolve.service';

const leaseLiabilityScheduleReportRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityScheduleReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityScheduleReportDetailComponent,
    resolve: {
      leaseLiabilityScheduleReport: LeaseLiabilityScheduleReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityScheduleReportUpdateComponent,
    resolve: {
      leaseLiabilityScheduleReport: LeaseLiabilityScheduleReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityScheduleReportUpdateComponent,
    resolve: {
      leaseLiabilityScheduleReport: LeaseLiabilityScheduleReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityScheduleReportRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityScheduleReportRoutingModule {}
