import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityComponent } from '../list/lease-liability.component';
import { LeaseLiabilityDetailComponent } from '../detail/lease-liability-detail.component';
import { LeaseLiabilityUpdateComponent } from '../update/lease-liability-update.component';
import { LeaseLiabilityRoutingResolveService } from './lease-liability-routing-resolve.service';

const leaseLiabilityRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityDetailComponent,
    resolve: {
      leaseLiability: LeaseLiabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityUpdateComponent,
    resolve: {
      leaseLiability: LeaseLiabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityUpdateComponent,
    resolve: {
      leaseLiability: LeaseLiabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityRoutingModule {}
