import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationReportComponent } from '../list/depreciation-report.component';
import { DepreciationReportDetailComponent } from '../detail/depreciation-report-detail.component';
import { DepreciationReportUpdateComponent } from '../update/depreciation-report-update.component';
import { DepreciationReportRoutingResolveService } from './depreciation-report-routing-resolve.service';

const depreciationReportRoute: Routes = [
  {
    path: '',
    component: DepreciationReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationReportDetailComponent,
    resolve: {
      depreciationReport: DepreciationReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationReportUpdateComponent,
    resolve: {
      depreciationReport: DepreciationReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationReportUpdateComponent,
    resolve: {
      depreciationReport: DepreciationReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationReportRoute)],
  exports: [RouterModule],
})
export class DepreciationReportRoutingModule {}
