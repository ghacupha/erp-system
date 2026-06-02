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
      defaultSort: 'id,asc',
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
