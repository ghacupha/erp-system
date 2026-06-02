import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsicEconomicActivityComponent } from '../list/isic-economic-activity.component';
import { IsicEconomicActivityDetailComponent } from '../detail/isic-economic-activity-detail.component';
import { IsicEconomicActivityUpdateComponent } from '../update/isic-economic-activity-update.component';
import { IsicEconomicActivityRoutingResolveService } from './isic-economic-activity-routing-resolve.service';

const isicEconomicActivityRoute: Routes = [
  {
    path: '',
    component: IsicEconomicActivityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsicEconomicActivityDetailComponent,
    resolve: {
      isicEconomicActivity: IsicEconomicActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsicEconomicActivityUpdateComponent,
    resolve: {
      isicEconomicActivity: IsicEconomicActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsicEconomicActivityUpdateComponent,
    resolve: {
      isicEconomicActivity: IsicEconomicActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isicEconomicActivityRoute)],
  exports: [RouterModule],
})
export class IsicEconomicActivityRoutingModule {}
