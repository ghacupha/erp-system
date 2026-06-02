import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardAcquiringTransactionComponent } from '../list/card-acquiring-transaction.component';
import { CardAcquiringTransactionDetailComponent } from '../detail/card-acquiring-transaction-detail.component';
import { CardAcquiringTransactionUpdateComponent } from '../update/card-acquiring-transaction-update.component';
import { CardAcquiringTransactionRoutingResolveService } from './card-acquiring-transaction-routing-resolve.service';

const cardAcquiringTransactionRoute: Routes = [
  {
    path: '',
    component: CardAcquiringTransactionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardAcquiringTransactionDetailComponent,
    resolve: {
      cardAcquiringTransaction: CardAcquiringTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardAcquiringTransactionUpdateComponent,
    resolve: {
      cardAcquiringTransaction: CardAcquiringTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardAcquiringTransactionUpdateComponent,
    resolve: {
      cardAcquiringTransaction: CardAcquiringTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardAcquiringTransactionRoute)],
  exports: [RouterModule],
})
export class CardAcquiringTransactionRoutingModule {}
