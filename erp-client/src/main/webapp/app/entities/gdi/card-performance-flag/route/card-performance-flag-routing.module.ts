import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardPerformanceFlagComponent } from '../list/card-performance-flag.component';
import { CardPerformanceFlagDetailComponent } from '../detail/card-performance-flag-detail.component';
import { CardPerformanceFlagUpdateComponent } from '../update/card-performance-flag-update.component';
import { CardPerformanceFlagRoutingResolveService } from './card-performance-flag-routing-resolve.service';

const cardPerformanceFlagRoute: Routes = [
  {
    path: '',
    component: CardPerformanceFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardPerformanceFlagDetailComponent,
    resolve: {
      cardPerformanceFlag: CardPerformanceFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardPerformanceFlagUpdateComponent,
    resolve: {
      cardPerformanceFlag: CardPerformanceFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardPerformanceFlagUpdateComponent,
    resolve: {
      cardPerformanceFlag: CardPerformanceFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardPerformanceFlagRoute)],
  exports: [RouterModule],
})
export class CardPerformanceFlagRoutingModule {}
