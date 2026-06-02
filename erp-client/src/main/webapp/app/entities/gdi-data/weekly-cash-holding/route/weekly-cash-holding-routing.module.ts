import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WeeklyCashHoldingComponent } from '../list/weekly-cash-holding.component';
import { WeeklyCashHoldingDetailComponent } from '../detail/weekly-cash-holding-detail.component';
import { WeeklyCashHoldingUpdateComponent } from '../update/weekly-cash-holding-update.component';
import { WeeklyCashHoldingRoutingResolveService } from './weekly-cash-holding-routing-resolve.service';

const weeklyCashHoldingRoute: Routes = [
  {
    path: '',
    component: WeeklyCashHoldingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WeeklyCashHoldingDetailComponent,
    resolve: {
      weeklyCashHolding: WeeklyCashHoldingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WeeklyCashHoldingUpdateComponent,
    resolve: {
      weeklyCashHolding: WeeklyCashHoldingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WeeklyCashHoldingUpdateComponent,
    resolve: {
      weeklyCashHolding: WeeklyCashHoldingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(weeklyCashHoldingRoute)],
  exports: [RouterModule],
})
export class WeeklyCashHoldingRoutingModule {}
