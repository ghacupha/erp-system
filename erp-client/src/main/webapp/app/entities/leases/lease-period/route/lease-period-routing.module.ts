import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeasePeriodComponent } from '../list/lease-period.component';
import { LeasePeriodDetailComponent } from '../detail/lease-period-detail.component';
import { LeasePeriodUpdateComponent } from '../update/lease-period-update.component';
import { LeasePeriodRoutingResolveService } from './lease-period-routing-resolve.service';

const leasePeriodRoute: Routes = [
  {
    path: '',
    component: LeasePeriodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeasePeriodDetailComponent,
    resolve: {
      leasePeriod: LeasePeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeasePeriodUpdateComponent,
    resolve: {
      leasePeriod: LeasePeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeasePeriodUpdateComponent,
    resolve: {
      leasePeriod: LeasePeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leasePeriodRoute)],
  exports: [RouterModule],
})
export class LeasePeriodRoutingModule {}
