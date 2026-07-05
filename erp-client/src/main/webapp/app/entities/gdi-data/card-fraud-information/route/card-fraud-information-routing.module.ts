import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardFraudInformationComponent } from '../list/card-fraud-information.component';
import { CardFraudInformationDetailComponent } from '../detail/card-fraud-information-detail.component';
import { CardFraudInformationUpdateComponent } from '../update/card-fraud-information-update.component';
import { CardFraudInformationRoutingResolveService } from './card-fraud-information-routing-resolve.service';

const cardFraudInformationRoute: Routes = [
  {
    path: '',
    component: CardFraudInformationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardFraudInformationDetailComponent,
    resolve: {
      cardFraudInformation: CardFraudInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardFraudInformationUpdateComponent,
    resolve: {
      cardFraudInformation: CardFraudInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardFraudInformationUpdateComponent,
    resolve: {
      cardFraudInformation: CardFraudInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardFraudInformationRoute)],
  exports: [RouterModule],
})
export class CardFraudInformationRoutingModule {}
