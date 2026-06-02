import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkInProgressOutstandingReportRequisitionComponent } from '../list/work-in-progress-outstanding-report-requisition.component';
import { WorkInProgressOutstandingReportRequisitionDetailComponent } from '../detail/work-in-progress-outstanding-report-requisition-detail.component';
import { WorkInProgressOutstandingReportRequisitionUpdateComponent } from '../update/work-in-progress-outstanding-report-requisition-update.component';
import { WorkInProgressOutstandingReportRequisitionRoutingResolveService } from './work-in-progress-outstanding-report-requisition-routing-resolve.service';

const workInProgressOutstandingReportRequisitionRoute: Routes = [
  {
    path: '',
    component: WorkInProgressOutstandingReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkInProgressOutstandingReportRequisitionDetailComponent,
    resolve: {
      workInProgressOutstandingReportRequisition: WorkInProgressOutstandingReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkInProgressOutstandingReportRequisitionUpdateComponent,
    resolve: {
      workInProgressOutstandingReportRequisition: WorkInProgressOutstandingReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkInProgressOutstandingReportRequisitionUpdateComponent,
    resolve: {
      workInProgressOutstandingReportRequisition: WorkInProgressOutstandingReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workInProgressOutstandingReportRequisitionRoute)],
  exports: [RouterModule],
})
export class WorkInProgressOutstandingReportRequisitionRoutingModule {}
