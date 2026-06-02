import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PdfReportRequisitionComponent } from '../list/pdf-report-requisition.component';
import { PdfReportRequisitionDetailComponent } from '../detail/pdf-report-requisition-detail.component';
import { PdfReportRequisitionUpdateComponent } from '../update/pdf-report-requisition-update.component';
import { PdfReportRequisitionRoutingResolveService } from './pdf-report-requisition-routing-resolve.service';

const pdfReportRequisitionRoute: Routes = [
  {
    path: '',
    component: PdfReportRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PdfReportRequisitionDetailComponent,
    resolve: {
      pdfReportRequisition: PdfReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PdfReportRequisitionUpdateComponent,
    resolve: {
      pdfReportRequisition: PdfReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PdfReportRequisitionUpdateComponent,
    resolve: {
      pdfReportRequisition: PdfReportRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pdfReportRequisitionRoute)],
  exports: [RouterModule],
})
export class PdfReportRequisitionRoutingModule {}
