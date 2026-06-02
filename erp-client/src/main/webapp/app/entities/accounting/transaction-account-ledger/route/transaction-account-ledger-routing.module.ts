import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionAccountLedgerComponent } from '../list/transaction-account-ledger.component';
import { TransactionAccountLedgerDetailComponent } from '../detail/transaction-account-ledger-detail.component';
import { TransactionAccountLedgerUpdateComponent } from '../update/transaction-account-ledger-update.component';
import { TransactionAccountLedgerRoutingResolveService } from './transaction-account-ledger-routing-resolve.service';

const transactionAccountLedgerRoute: Routes = [
  {
    path: '',
    component: TransactionAccountLedgerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountLedgerDetailComponent,
    resolve: {
      transactionAccountLedger: TransactionAccountLedgerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionAccountLedgerUpdateComponent,
    resolve: {
      transactionAccountLedger: TransactionAccountLedgerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionAccountLedgerUpdateComponent,
    resolve: {
      transactionAccountLedger: TransactionAccountLedgerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionAccountLedgerRoute)],
  exports: [RouterModule],
})
export class TransactionAccountLedgerRoutingModule {}
