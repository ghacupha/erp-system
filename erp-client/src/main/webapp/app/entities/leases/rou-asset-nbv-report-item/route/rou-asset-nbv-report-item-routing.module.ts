import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouAssetNBVReportItemComponent } from '../list/rou-asset-nbv-report-item.component';
import { RouAssetNBVReportItemDetailComponent } from '../detail/rou-asset-nbv-report-item-detail.component';
import { RouAssetNBVReportItemRoutingResolveService } from './rou-asset-nbv-report-item-routing-resolve.service';

const rouAssetNBVReportItemRoute: Routes = [
  {
    path: '',
    component: RouAssetNBVReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouAssetNBVReportItemDetailComponent,
    resolve: {
      rouAssetNBVReportItem: RouAssetNBVReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouAssetNBVReportItemRoute)],
  exports: [RouterModule],
})
export class RouAssetNBVReportItemRoutingModule {}
