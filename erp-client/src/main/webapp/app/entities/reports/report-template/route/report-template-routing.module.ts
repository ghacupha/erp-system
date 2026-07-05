import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportTemplateComponent } from '../list/report-template.component';
import { ReportTemplateDetailComponent } from '../detail/report-template-detail.component';
import { ReportTemplateUpdateComponent } from '../update/report-template-update.component';
import { ReportTemplateRoutingResolveService } from './report-template-routing-resolve.service';

const reportTemplateRoute: Routes = [
  {
    path: '',
    component: ReportTemplateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportTemplateDetailComponent,
    resolve: {
      reportTemplate: ReportTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportTemplateUpdateComponent,
    resolve: {
      reportTemplate: ReportTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportTemplateUpdateComponent,
    resolve: {
      reportTemplate: ReportTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportTemplateRoute)],
  exports: [RouterModule],
})
export class ReportTemplateRoutingModule {}
