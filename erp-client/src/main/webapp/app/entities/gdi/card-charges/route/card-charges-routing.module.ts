import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardChargesComponent } from '../list/card-charges.component';
import { CardChargesDetailComponent } from '../detail/card-charges-detail.component';
import { CardChargesUpdateComponent } from '../update/card-charges-update.component';
import { CardChargesRoutingResolveService } from './card-charges-routing-resolve.service';

const cardChargesRoute: Routes = [
  {
    path: '',
    component: CardChargesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardChargesDetailComponent,
    resolve: {
      cardCharges: CardChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardChargesUpdateComponent,
    resolve: {
      cardCharges: CardChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardChargesUpdateComponent,
    resolve: {
      cardCharges: CardChargesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardChargesRoute)],
  exports: [RouterModule],
})
export class CardChargesRoutingModule {}
