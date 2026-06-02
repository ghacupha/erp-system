import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouDepreciationPostingReportComponent } from '../list/rou-depreciation-posting-report.component';
import { RouDepreciationPostingReportDetailComponent } from '../detail/rou-depreciation-posting-report-detail.component';
import { RouDepreciationPostingReportUpdateComponent } from '../update/rou-depreciation-posting-report-update.component';
import { RouDepreciationPostingReportRoutingResolveService } from './rou-depreciation-posting-report-routing-resolve.service';

const rouDepreciationPostingReportRoute: Routes = [
  {
    path: '',
    component: RouDepreciationPostingReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouDepreciationPostingReportDetailComponent,
    resolve: {
      rouDepreciationPostingReport: RouDepreciationPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouDepreciationPostingReportUpdateComponent,
    resolve: {
      rouDepreciationPostingReport: RouDepreciationPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouDepreciationPostingReportUpdateComponent,
    resolve: {
      rouDepreciationPostingReport: RouDepreciationPostingReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouDepreciationPostingReportRoute)],
  exports: [RouterModule],
})
export class RouDepreciationPostingReportRoutingModule {}
