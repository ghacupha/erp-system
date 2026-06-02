import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouDepreciationEntryReportComponent } from '../list/rou-depreciation-entry-report.component';
import { RouDepreciationEntryReportDetailComponent } from '../detail/rou-depreciation-entry-report-detail.component';
import { RouDepreciationEntryReportUpdateComponent } from '../update/rou-depreciation-entry-report-update.component';
import { RouDepreciationEntryReportRoutingResolveService } from './rou-depreciation-entry-report-routing-resolve.service';

const rouDepreciationEntryReportRoute: Routes = [
  {
    path: '',
    component: RouDepreciationEntryReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouDepreciationEntryReportDetailComponent,
    resolve: {
      rouDepreciationEntryReport: RouDepreciationEntryReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouDepreciationEntryReportUpdateComponent,
    resolve: {
      rouDepreciationEntryReport: RouDepreciationEntryReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouDepreciationEntryReportUpdateComponent,
    resolve: {
      rouDepreciationEntryReport: RouDepreciationEntryReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouDepreciationEntryReportRoute)],
  exports: [RouterModule],
})
export class RouDepreciationEntryReportRoutingModule {}
