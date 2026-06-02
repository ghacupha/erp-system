import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RouMonthlyDepreciationReportItemComponent } from '../list/rou-monthly-depreciation-report-item.component';
import { RouMonthlyDepreciationReportItemDetailComponent } from '../detail/rou-monthly-depreciation-report-item-detail.component';
import { RouMonthlyDepreciationReportItemUpdateComponent } from '../update/rou-monthly-depreciation-report-item-update.component';
import { RouMonthlyDepreciationReportItemRoutingResolveService } from './rou-monthly-depreciation-report-item-routing-resolve.service';

const rouMonthlyDepreciationReportItemRoute: Routes = [
  {
    path: '',
    component: RouMonthlyDepreciationReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RouMonthlyDepreciationReportItemDetailComponent,
    resolve: {
      rouMonthlyDepreciationReportItem: RouMonthlyDepreciationReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RouMonthlyDepreciationReportItemUpdateComponent,
    resolve: {
      rouMonthlyDepreciationReportItem: RouMonthlyDepreciationReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RouMonthlyDepreciationReportItemUpdateComponent,
    resolve: {
      rouMonthlyDepreciationReportItem: RouMonthlyDepreciationReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rouMonthlyDepreciationReportItemRoute)],
  exports: [RouterModule],
})
export class RouMonthlyDepreciationReportItemRoutingModule {}
