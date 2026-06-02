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
