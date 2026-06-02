import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmortizationPostingReportRequisitionComponent } from '../list/amortization-posting-report-requisition.component';
import { AmortizationPostingReportRequisitionDetailComponent } from '../detail/amortization-posting-report-requisition-detail.component';
import { AmortizationPostingReportRequisitionUpdateComponent } from '../update/amortization-posting-report-requisition-update.component';
import { AmortizationPostingReportRequisitionRoutingResolveService } from './amortization-posting-report-requisition-routing-resolve.service';

const amortizationPostingReportRequisitionRoute: Routes = [
  {
    path: '',
    component: AmortizationPostingReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmortizationPostingReportRequisitionDetailComponent,
    resolve: {
      amortizationPostingReportRequisition: AmortizationPostingReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmortizationPostingReportRequisitionUpdateComponent,
    resolve: {
      amortizationPostingReportRequisition: AmortizationPostingReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmortizationPostingReportRequisitionUpdateComponent,
    resolve: {
      amortizationPostingReportRequisition: AmortizationPostingReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amortizationPostingReportRequisitionRoute)],
  exports: [RouterModule],
})
export class AmortizationPostingReportRequisitionRoutingModule {}
