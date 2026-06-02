import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XlsxReportRequisitionComponent } from '../list/xlsx-report-requisition.component';
import { XlsxReportRequisitionDetailComponent } from '../detail/xlsx-report-requisition-detail.component';
import { XlsxReportRequisitionUpdateComponent } from '../update/xlsx-report-requisition-update.component';
import { XlsxReportRequisitionRoutingResolveService } from './xlsx-report-requisition-routing-resolve.service';

const xlsxReportRequisitionRoute: Routes = [
  {
    path: '',
    component: XlsxReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XlsxReportRequisitionDetailComponent,
    resolve: {
      xlsxReportRequisition: XlsxReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XlsxReportRequisitionUpdateComponent,
    resolve: {
      xlsxReportRequisition: XlsxReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XlsxReportRequisitionUpdateComponent,
    resolve: {
      xlsxReportRequisition: XlsxReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xlsxReportRequisitionRoute)],
  exports: [RouterModule],
})
export class XlsxReportRequisitionRoutingModule {}
