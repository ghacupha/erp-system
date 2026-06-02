import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardBrandTypeComponent } from '../list/card-brand-type.component';
import { CardBrandTypeDetailComponent } from '../detail/card-brand-type-detail.component';
import { CardBrandTypeUpdateComponent } from '../update/card-brand-type-update.component';
import { CardBrandTypeRoutingResolveService } from './card-brand-type-routing-resolve.service';

const cardBrandTypeRoute: Routes = [
  {
    path: '',
    component: CardBrandTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardBrandTypeDetailComponent,
    resolve: {
      cardBrandType: CardBrandTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardBrandTypeUpdateComponent,
    resolve: {
      cardBrandType: CardBrandTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardBrandTypeUpdateComponent,
    resolve: {
      cardBrandType: CardBrandTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardBrandTypeRoute)],
  exports: [RouterModule],
})
export class CardBrandTypeRoutingModule {}
