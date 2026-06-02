import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityScheduleReportItemComponent } from '../list/lease-liability-schedule-report-item.component';
import { LeaseLiabilityScheduleReportItemDetailComponent } from '../detail/lease-liability-schedule-report-item-detail.component';
import { LeaseLiabilityScheduleReportItemRoutingResolveService } from './lease-liability-schedule-report-item-routing-resolve.service';

const leaseLiabilityScheduleReportItemRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityScheduleReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityScheduleReportItemDetailComponent,
    resolve: {
      leaseLiabilityScheduleReportItem: LeaseLiabilityScheduleReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityScheduleReportItemRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityScheduleReportItemRoutingModule {}
