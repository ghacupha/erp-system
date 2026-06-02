import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionAccountComponent } from '../list/transaction-account.component';
import { TransactionAccountDetailComponent } from '../detail/transaction-account-detail.component';
import { TransactionAccountUpdateComponent } from '../update/transaction-account-update.component';
import { TransactionAccountRoutingResolveService } from './transaction-account-routing-resolve.service';

const transactionAccountRoute: Routes = [
  {
    path: '',
    component: TransactionAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountDetailComponent,
    resolve: {
      transactionAccount: TransactionAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionAccountUpdateComponent,
    resolve: {
      transactionAccount: TransactionAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionAccountUpdateComponent,
    resolve: {
      transactionAccount: TransactionAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionAccountRoute)],
  exports: [RouterModule],
})
export class TransactionAccountRoutingModule {}
