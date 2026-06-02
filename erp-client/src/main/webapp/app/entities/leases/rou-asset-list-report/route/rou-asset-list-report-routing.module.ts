import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouAssetListReportComponent } from '../list/rou-asset-list-report.component';
import { RouAssetListReportDetailComponent } from '../detail/rou-asset-list-report-detail.component';
import { RouAssetListReportUpdateComponent } from '../update/rou-asset-list-report-update.component';
import { RouAssetListReportRoutingResolveService } from './rou-asset-list-report-routing-resolve.service';

const rouAssetListReportRoute: Routes = [
  {
    path: '',
    component: RouAssetListReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouAssetListReportDetailComponent,
    resolve: {
      rouAssetListReport: RouAssetListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouAssetListReportUpdateComponent,
    resolve: {
      rouAssetListReport: RouAssetListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouAssetListReportUpdateComponent,
    resolve: {
      rouAssetListReport: RouAssetListReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouAssetListReportRoute)],
  exports: [RouterModule],
})
export class RouAssetListReportRoutingModule {}
