import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbReportRequestReasonsComponent } from '../list/crb-report-request-reasons.component';
import { CrbReportRequestReasonsDetailComponent } from '../detail/crb-report-request-reasons-detail.component';
import { CrbReportRequestReasonsUpdateComponent } from '../update/crb-report-request-reasons-update.component';
import { CrbReportRequestReasonsRoutingResolveService } from './crb-report-request-reasons-routing-resolve.service';

const crbReportRequestReasonsRoute: Routes = [
  {
    path: '',
    component: CrbReportRequestReasonsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbReportRequestReasonsDetailComponent,
    resolve: {
      crbReportRequestReasons: CrbReportRequestReasonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbReportRequestReasonsUpdateComponent,
    resolve: {
      crbReportRequestReasons: CrbReportRequestReasonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbReportRequestReasonsUpdateComponent,
    resolve: {
      crbReportRequestReasons: CrbReportRequestReasonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbReportRequestReasonsRoute)],
  exports: [RouterModule],
})
export class CrbReportRequestReasonsRoutingModule {}
