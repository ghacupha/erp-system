import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseAmortizationScheduleComponent } from '../list/lease-amortization-schedule.component';
import { LeaseAmortizationScheduleDetailComponent } from '../detail/lease-amortization-schedule-detail.component';
import { LeaseAmortizationScheduleUpdateComponent } from '../update/lease-amortization-schedule-update.component';
import { LeaseAmortizationScheduleRoutingResolveService } from './lease-amortization-schedule-routing-resolve.service';

const leaseAmortizationScheduleRoute: Routes = [
  {
    path: '',
    component: LeaseAmortizationScheduleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseAmortizationScheduleDetailComponent,
    resolve: {
      leaseAmortizationSchedule: LeaseAmortizationScheduleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseAmortizationScheduleUpdateComponent,
    resolve: {
      leaseAmortizationSchedule: LeaseAmortizationScheduleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseAmortizationScheduleUpdateComponent,
    resolve: {
      leaseAmortizationSchedule: LeaseAmortizationScheduleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseAmortizationScheduleRoute)],
  exports: [RouterModule],
})
export class LeaseAmortizationScheduleRoutingModule {}
