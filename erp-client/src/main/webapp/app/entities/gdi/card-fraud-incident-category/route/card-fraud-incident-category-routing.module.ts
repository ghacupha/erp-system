import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardFraudIncidentCategoryComponent } from '../list/card-fraud-incident-category.component';
import { CardFraudIncidentCategoryDetailComponent } from '../detail/card-fraud-incident-category-detail.component';
import { CardFraudIncidentCategoryUpdateComponent } from '../update/card-fraud-incident-category-update.component';
import { CardFraudIncidentCategoryRoutingResolveService } from './card-fraud-incident-category-routing-resolve.service';

const cardFraudIncidentCategoryRoute: Routes = [
  {
    path: '',
    component: CardFraudIncidentCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardFraudIncidentCategoryDetailComponent,
    resolve: {
      cardFraudIncidentCategory: CardFraudIncidentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardFraudIncidentCategoryUpdateComponent,
    resolve: {
      cardFraudIncidentCategory: CardFraudIncidentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardFraudIncidentCategoryUpdateComponent,
    resolve: {
      cardFraudIncidentCategory: CardFraudIncidentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardFraudIncidentCategoryRoute)],
  exports: [RouterModule],
})
export class CardFraudIncidentCategoryRoutingModule {}
