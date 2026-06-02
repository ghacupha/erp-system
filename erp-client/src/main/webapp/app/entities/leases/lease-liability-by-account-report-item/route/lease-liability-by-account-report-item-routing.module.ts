import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityByAccountReportItemComponent } from '../list/lease-liability-by-account-report-item.component';
import { LeaseLiabilityByAccountReportItemDetailComponent } from '../detail/lease-liability-by-account-report-item-detail.component';
import { LeaseLiabilityByAccountReportItemRoutingResolveService } from './lease-liability-by-account-report-item-routing-resolve.service';

const leaseLiabilityByAccountReportItemRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityByAccountReportItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityByAccountReportItemDetailComponent,
    resolve: {
      leaseLiabilityByAccountReportItem: LeaseLiabilityByAccountReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityByAccountReportItemRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityByAccountReportItemRoutingModule {}
