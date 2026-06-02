import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityScheduleItemComponent } from '../list/lease-liability-schedule-item.component';
import { LeaseLiabilityScheduleItemDetailComponent } from '../detail/lease-liability-schedule-item-detail.component';
import { LeaseLiabilityScheduleItemUpdateComponent } from '../update/lease-liability-schedule-item-update.component';
import { LeaseLiabilityScheduleItemRoutingResolveService } from './lease-liability-schedule-item-routing-resolve.service';

const leaseLiabilityScheduleItemRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityScheduleItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityScheduleItemDetailComponent,
    resolve: {
      leaseLiabilityScheduleItem: LeaseLiabilityScheduleItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityScheduleItemUpdateComponent,
    resolve: {
      leaseLiabilityScheduleItem: LeaseLiabilityScheduleItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityScheduleItemUpdateComponent,
    resolve: {
      leaseLiabilityScheduleItem: LeaseLiabilityScheduleItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityScheduleItemRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityScheduleItemRoutingModule {}
