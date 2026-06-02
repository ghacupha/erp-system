import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouDepreciationPostingReportItemComponent } from '../list/rou-depreciation-posting-report-item.component';
import { RouDepreciationPostingReportItemDetailComponent } from '../detail/rou-depreciation-posting-report-item-detail.component';
import { RouDepreciationPostingReportItemRoutingResolveService } from './rou-depreciation-posting-report-item-routing-resolve.service';

const rouDepreciationPostingReportItemRoute: Routes = [
  {
    path: '',
    component: RouDepreciationPostingReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouDepreciationPostingReportItemDetailComponent,
    resolve: {
      rouDepreciationPostingReportItem: RouDepreciationPostingReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouDepreciationPostingReportItemRoute)],
  exports: [RouterModule],
})
export class RouDepreciationPostingReportItemRoutingModule {}
