import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WeeklyCounterfeitHoldingComponent } from '../list/weekly-counterfeit-holding.component';
import { WeeklyCounterfeitHoldingDetailComponent } from '../detail/weekly-counterfeit-holding-detail.component';
import { WeeklyCounterfeitHoldingUpdateComponent } from '../update/weekly-counterfeit-holding-update.component';
import { WeeklyCounterfeitHoldingRoutingResolveService } from './weekly-counterfeit-holding-routing-resolve.service';

const weeklyCounterfeitHoldingRoute: Routes = [
  {
    path: '',
    component: WeeklyCounterfeitHoldingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WeeklyCounterfeitHoldingDetailComponent,
    resolve: {
      weeklyCounterfeitHolding: WeeklyCounterfeitHoldingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WeeklyCounterfeitHoldingUpdateComponent,
    resolve: {
      weeklyCounterfeitHolding: WeeklyCounterfeitHoldingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WeeklyCounterfeitHoldingUpdateComponent,
    resolve: {
      weeklyCounterfeitHolding: WeeklyCounterfeitHoldingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(weeklyCounterfeitHoldingRoute)],
  exports: [RouterModule],
})
export class WeeklyCounterfeitHoldingRoutingModule {}
