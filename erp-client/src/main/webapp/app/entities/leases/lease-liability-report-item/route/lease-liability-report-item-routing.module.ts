import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityReportItemComponent } from '../list/lease-liability-report-item.component';
import { LeaseLiabilityReportItemDetailComponent } from '../detail/lease-liability-report-item-detail.component';
import { LeaseLiabilityReportItemRoutingResolveService } from './lease-liability-report-item-routing-resolve.service';

const leaseLiabilityReportItemRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityReportItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityReportItemDetailComponent,
    resolve: {
      leaseLiabilityReportItem: LeaseLiabilityReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityReportItemRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityReportItemRoutingModule {}
