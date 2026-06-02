import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardIssuerChargesComponent } from '../list/card-issuer-charges.component';
import { CardIssuerChargesDetailComponent } from '../detail/card-issuer-charges-detail.component';
import { CardIssuerChargesUpdateComponent } from '../update/card-issuer-charges-update.component';
import { CardIssuerChargesRoutingResolveService } from './card-issuer-charges-routing-resolve.service';

const cardIssuerChargesRoute: Routes = [
  {
    path: '',
    component: CardIssuerChargesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardIssuerChargesDetailComponent,
    resolve: {
      cardIssuerCharges: CardIssuerChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardIssuerChargesUpdateComponent,
    resolve: {
      cardIssuerCharges: CardIssuerChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardIssuerChargesUpdateComponent,
    resolve: {
      cardIssuerCharges: CardIssuerChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardIssuerChargesRoute)],
  exports: [RouterModule],
})
export class CardIssuerChargesRoutingModule {}
