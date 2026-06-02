import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardTypesComponent } from '../list/card-types.component';
import { CardTypesDetailComponent } from '../detail/card-types-detail.component';
import { CardTypesUpdateComponent } from '../update/card-types-update.component';
import { CardTypesRoutingResolveService } from './card-types-routing-resolve.service';

const cardTypesRoute: Routes = [
  {
    path: '',
    component: CardTypesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardTypesDetailComponent,
    resolve: {
      cardTypes: CardTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardTypesUpdateComponent,
    resolve: {
      cardTypes: CardTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardTypesUpdateComponent,
    resolve: {
      cardTypes: CardTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardTypesRoute)],
  exports: [RouterModule],
})
export class CardTypesRoutingModule {}
