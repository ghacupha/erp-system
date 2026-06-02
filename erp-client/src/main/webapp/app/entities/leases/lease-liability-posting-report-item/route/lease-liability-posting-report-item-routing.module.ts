import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityPostingReportItemComponent } from '../list/lease-liability-posting-report-item.component';
import { LeaseLiabilityPostingReportItemDetailComponent } from '../detail/lease-liability-posting-report-item-detail.component';
import { LeaseLiabilityPostingReportItemRoutingResolveService } from './lease-liability-posting-report-item-routing-resolve.service';

const leaseLiabilityPostingReportItemRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityPostingReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityPostingReportItemDetailComponent,
    resolve: {
      leaseLiabilityPostingReportItem: LeaseLiabilityPostingReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityPostingReportItemRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityPostingReportItemRoutingModule {}
