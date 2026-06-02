import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouAssetListReportItemComponent } from '../list/rou-asset-list-report-item.component';
import { RouAssetListReportItemDetailComponent } from '../detail/rou-asset-list-report-item-detail.component';
import { RouAssetListReportItemRoutingResolveService } from './rou-asset-list-report-item-routing-resolve.service';

const rouAssetListReportItemRoute: Routes = [
  {
    path: '',
    component: RouAssetListReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouAssetListReportItemDetailComponent,
    resolve: {
      rouAssetListReportItem: RouAssetListReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouAssetListReportItemRoute)],
  exports: [RouterModule],
})
export class RouAssetListReportItemRoutingModule {}
