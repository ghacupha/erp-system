import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardCategoryTypeComponent } from '../list/card-category-type.component';
import { CardCategoryTypeDetailComponent } from '../detail/card-category-type-detail.component';
import { CardCategoryTypeUpdateComponent } from '../update/card-category-type-update.component';
import { CardCategoryTypeRoutingResolveService } from './card-category-type-routing-resolve.service';

const cardCategoryTypeRoute: Routes = [
  {
    path: '',
    component: CardCategoryTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardCategoryTypeDetailComponent,
    resolve: {
      cardCategoryType: CardCategoryTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardCategoryTypeUpdateComponent,
    resolve: {
      cardCategoryType: CardCategoryTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardCategoryTypeUpdateComponent,
    resolve: {
      cardCategoryType: CardCategoryTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardCategoryTypeRoute)],
  exports: [RouterModule],
})
export class CardCategoryTypeRoutingModule {}
