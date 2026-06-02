import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbReportViewBandComponent } from '../list/crb-report-view-band.component';
import { CrbReportViewBandDetailComponent } from '../detail/crb-report-view-band-detail.component';
import { CrbReportViewBandUpdateComponent } from '../update/crb-report-view-band-update.component';
import { CrbReportViewBandRoutingResolveService } from './crb-report-view-band-routing-resolve.service';

const crbReportViewBandRoute: Routes = [
  {
    path: '',
    component: CrbReportViewBandComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbReportViewBandDetailComponent,
    resolve: {
      crbReportViewBand: CrbReportViewBandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbReportViewBandUpdateComponent,
    resolve: {
      crbReportViewBand: CrbReportViewBandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbReportViewBandUpdateComponent,
    resolve: {
      crbReportViewBand: CrbReportViewBandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbReportViewBandRoute)],
  exports: [RouterModule],
})
export class CrbReportViewBandRoutingModule {}
