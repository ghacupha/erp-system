import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouMonthlyDepreciationReportComponent } from '../list/rou-monthly-depreciation-report.component';
import { RouMonthlyDepreciationReportDetailComponent } from '../detail/rou-monthly-depreciation-report-detail.component';
import { RouMonthlyDepreciationReportUpdateComponent } from '../update/rou-monthly-depreciation-report-update.component';
import { RouMonthlyDepreciationReportRoutingResolveService } from './rou-monthly-depreciation-report-routing-resolve.service';

const rouMonthlyDepreciationReportRoute: Routes = [
  {
    path: '',
    component: RouMonthlyDepreciationReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouMonthlyDepreciationReportDetailComponent,
    resolve: {
      rouMonthlyDepreciationReport: RouMonthlyDepreciationReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouMonthlyDepreciationReportUpdateComponent,
    resolve: {
      rouMonthlyDepreciationReport: RouMonthlyDepreciationReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouMonthlyDepreciationReportUpdateComponent,
    resolve: {
      rouMonthlyDepreciationReport: RouMonthlyDepreciationReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouMonthlyDepreciationReportRoute)],
  exports: [RouterModule],
})
export class RouMonthlyDepreciationReportRoutingModule {}
