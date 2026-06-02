import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NbvReportComponent } from '../list/nbv-report.component';
import { NbvReportDetailComponent } from '../detail/nbv-report-detail.component';
import { NbvReportUpdateComponent } from '../update/nbv-report-update.component';
import { NbvReportRoutingResolveService } from './nbv-report-routing-resolve.service';

const nbvReportRoute: Routes = [
  {
    path: '',
    component: NbvReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NbvReportDetailComponent,
    resolve: {
      nbvReport: NbvReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NbvReportUpdateComponent,
    resolve: {
      nbvReport: NbvReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NbvReportUpdateComponent,
    resolve: {
      nbvReport: NbvReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nbvReportRoute)],
  exports: [RouterModule],
})
export class NbvReportRoutingModule {}
