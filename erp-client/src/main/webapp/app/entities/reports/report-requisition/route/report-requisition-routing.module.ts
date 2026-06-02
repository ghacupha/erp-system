import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportRequisitionComponent } from '../list/report-requisition.component';
import { ReportRequisitionDetailComponent } from '../detail/report-requisition-detail.component';
import { ReportRequisitionUpdateComponent } from '../update/report-requisition-update.component';
import { ReportRequisitionRoutingResolveService } from './report-requisition-routing-resolve.service';

const reportRequisitionRoute: Routes = [
  {
    path: '',
    component: ReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportRequisitionDetailComponent,
    resolve: {
      reportRequisition: ReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportRequisitionUpdateComponent,
    resolve: {
      reportRequisition: ReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportRequisitionUpdateComponent,
    resolve: {
      reportRequisition: ReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportRequisitionRoute)],
  exports: [RouterModule],
})
export class ReportRequisitionRoutingModule {}
