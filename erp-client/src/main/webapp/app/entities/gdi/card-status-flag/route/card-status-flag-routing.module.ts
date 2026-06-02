import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardStatusFlagComponent } from '../list/card-status-flag.component';
import { CardStatusFlagDetailComponent } from '../detail/card-status-flag-detail.component';
import { CardStatusFlagUpdateComponent } from '../update/card-status-flag-update.component';
import { CardStatusFlagRoutingResolveService } from './card-status-flag-routing-resolve.service';

const cardStatusFlagRoute: Routes = [
  {
    path: '',
    component: CardStatusFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardStatusFlagDetailComponent,
    resolve: {
      cardStatusFlag: CardStatusFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardStatusFlagUpdateComponent,
    resolve: {
      cardStatusFlag: CardStatusFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardStatusFlagUpdateComponent,
    resolve: {
      cardStatusFlag: CardStatusFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardStatusFlagRoute)],
  exports: [RouterModule],
})
export class CardStatusFlagRoutingModule {}
