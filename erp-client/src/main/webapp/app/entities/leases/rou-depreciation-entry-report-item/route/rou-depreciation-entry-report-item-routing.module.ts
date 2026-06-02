import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouDepreciationEntryReportItemComponent } from '../list/rou-depreciation-entry-report-item.component';
import { RouDepreciationEntryReportItemDetailComponent } from '../detail/rou-depreciation-entry-report-item-detail.component';
import { RouDepreciationEntryReportItemRoutingResolveService } from './rou-depreciation-entry-report-item-routing-resolve.service';

const rouDepreciationEntryReportItemRoute: Routes = [
  {
    path: '',
    component: RouDepreciationEntryReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouDepreciationEntryReportItemDetailComponent,
    resolve: {
      rouDepreciationEntryReportItem: RouDepreciationEntryReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouDepreciationEntryReportItemRoute)],
  exports: [RouterModule],
})
export class RouDepreciationEntryReportItemRoutingModule {}
