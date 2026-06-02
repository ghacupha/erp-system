import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionDetailsComponent } from '../list/transaction-details.component';
import { TransactionDetailsDetailComponent } from '../detail/transaction-details-detail.component';
import { TransactionDetailsUpdateComponent } from '../update/transaction-details-update.component';
import { TransactionDetailsRoutingResolveService } from './transaction-details-routing-resolve.service';

const transactionDetailsRoute: Routes = [
  {
    path: '',
    component: TransactionDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionDetailsDetailComponent,
    resolve: {
      transactionDetails: TransactionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionDetailsUpdateComponent,
    resolve: {
      transactionDetails: TransactionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionDetailsUpdateComponent,
    resolve: {
      transactionDetails: TransactionDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionDetailsRoute)],
  exports: [RouterModule],
})
export class TransactionDetailsRoutingModule {}
