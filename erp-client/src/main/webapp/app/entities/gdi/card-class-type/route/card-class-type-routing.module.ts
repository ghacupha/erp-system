import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardClassTypeComponent } from '../list/card-class-type.component';
import { CardClassTypeDetailComponent } from '../detail/card-class-type-detail.component';
import { CardClassTypeUpdateComponent } from '../update/card-class-type-update.component';
import { CardClassTypeRoutingResolveService } from './card-class-type-routing-resolve.service';

const cardClassTypeRoute: Routes = [
  {
    path: '',
    component: CardClassTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardClassTypeDetailComponent,
    resolve: {
      cardClassType: CardClassTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardClassTypeUpdateComponent,
    resolve: {
      cardClassType: CardClassTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardClassTypeUpdateComponent,
    resolve: {
      cardClassType: CardClassTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardClassTypeRoute)],
  exports: [RouterModule],
})
export class CardClassTypeRoutingModule {}
