import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MonthlyPrepaymentReportRequisitionComponent } from '../list/monthly-prepayment-report-requisition.component';
import { MonthlyPrepaymentReportRequisitionDetailComponent } from '../detail/monthly-prepayment-report-requisition-detail.component';
import { MonthlyPrepaymentReportRequisitionUpdateComponent } from '../update/monthly-prepayment-report-requisition-update.component';
import { MonthlyPrepaymentReportRequisitionRoutingResolveService } from './monthly-prepayment-report-requisition-routing-resolve.service';

const monthlyPrepaymentReportRequisitionRoute: Routes = [
  {
    path: '',
    component: MonthlyPrepaymentReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MonthlyPrepaymentReportRequisitionDetailComponent,
    resolve: {
      monthlyPrepaymentReportRequisition: MonthlyPrepaymentReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MonthlyPrepaymentReportRequisitionUpdateComponent,
    resolve: {
      monthlyPrepaymentReportRequisition: MonthlyPrepaymentReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MonthlyPrepaymentReportRequisitionUpdateComponent,
    resolve: {
      monthlyPrepaymentReportRequisition: MonthlyPrepaymentReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(monthlyPrepaymentReportRequisitionRoute)],
  exports: [RouterModule],
})
export class MonthlyPrepaymentReportRequisitionRoutingModule {}
