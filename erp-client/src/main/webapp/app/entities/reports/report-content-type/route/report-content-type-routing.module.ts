import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportContentTypeComponent } from '../list/report-content-type.component';
import { ReportContentTypeDetailComponent } from '../detail/report-content-type-detail.component';
import { ReportContentTypeUpdateComponent } from '../update/report-content-type-update.component';
import { ReportContentTypeRoutingResolveService } from './report-content-type-routing-resolve.service';

const reportContentTypeRoute: Routes = [
  {
    path: '',
    component: ReportContentTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportContentTypeDetailComponent,
    resolve: {
      reportContentType: ReportContentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportContentTypeUpdateComponent,
    resolve: {
      reportContentType: ReportContentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportContentTypeUpdateComponent,
    resolve: {
      reportContentType: ReportContentTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportContentTypeRoute)],
  exports: [RouterModule],
})
export class ReportContentTypeRoutingModule {}
