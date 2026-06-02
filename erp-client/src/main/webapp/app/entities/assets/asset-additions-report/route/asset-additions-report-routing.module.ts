import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AssetAdditionsReportComponent } from '../list/asset-additions-report.component';
import { AssetAdditionsReportDetailComponent } from '../detail/asset-additions-report-detail.component';
import { AssetAdditionsReportUpdateComponent } from '../update/asset-additions-report-update.component';
import { AssetAdditionsReportRoutingResolveService } from './asset-additions-report-routing-resolve.service';

const assetAdditionsReportRoute: Routes = [
  {
    path: '',
    component: AssetAdditionsReportComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssetAdditionsReportDetailComponent,
    resolve: {
      assetAdditionsReport: AssetAdditionsReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssetAdditionsReportUpdateComponent,
    resolve: {
      assetAdditionsReport: AssetAdditionsReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssetAdditionsReportUpdateComponent,
    resolve: {
      assetAdditionsReport: AssetAdditionsReportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(assetAdditionsReportRoute)],
  exports: [RouterModule],
})
export class AssetAdditionsReportRoutingModule {}
