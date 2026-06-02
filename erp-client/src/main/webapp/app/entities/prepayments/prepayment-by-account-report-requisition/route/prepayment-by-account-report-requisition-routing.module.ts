import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentByAccountReportRequisitionComponent } from '../list/prepayment-by-account-report-requisition.component';
import { PrepaymentByAccountReportRequisitionDetailComponent } from '../detail/prepayment-by-account-report-requisition-detail.component';
import { PrepaymentByAccountReportRequisitionUpdateComponent } from '../update/prepayment-by-account-report-requisition-update.component';
import { PrepaymentByAccountReportRequisitionRoutingResolveService } from './prepayment-by-account-report-requisition-routing-resolve.service';

const prepaymentByAccountReportRequisitionRoute: Routes = [
  {
    path: '',
    component: PrepaymentByAccountReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentByAccountReportRequisitionDetailComponent,
    resolve: {
      prepaymentByAccountReportRequisition: PrepaymentByAccountReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentByAccountReportRequisitionUpdateComponent,
    resolve: {
      prepaymentByAccountReportRequisition: PrepaymentByAccountReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentByAccountReportRequisitionUpdateComponent,
    resolve: {
      prepaymentByAccountReportRequisition: PrepaymentByAccountReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentByAccountReportRequisitionRoute)],
  exports: [RouterModule],
})
export class PrepaymentByAccountReportRequisitionRoutingModule {}
