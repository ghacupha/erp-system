import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouAssetNBVReportComponent } from '../list/rou-asset-nbv-report.component';
import { RouAssetNBVReportDetailComponent } from '../detail/rou-asset-nbv-report-detail.component';
import { RouAssetNBVReportUpdateComponent } from '../update/rou-asset-nbv-report-update.component';
import { RouAssetNBVReportRoutingResolveService } from './rou-asset-nbv-report-routing-resolve.service';

const rouAssetNBVReportRoute: Routes = [
  {
    path: '',
    component: RouAssetNBVReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouAssetNBVReportDetailComponent,
    resolve: {
      rouAssetNBVReport: RouAssetNBVReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouAssetNBVReportUpdateComponent,
    resolve: {
      rouAssetNBVReport: RouAssetNBVReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouAssetNBVReportUpdateComponent,
    resolve: {
      rouAssetNBVReport: RouAssetNBVReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouAssetNBVReportRoute)],
  exports: [RouterModule],
})
export class RouAssetNBVReportRoutingModule {}
