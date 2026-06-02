import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankTransactionTypeComponent } from '../list/bank-transaction-type.component';
import { BankTransactionTypeDetailComponent } from '../detail/bank-transaction-type-detail.component';
import { BankTransactionTypeUpdateComponent } from '../update/bank-transaction-type-update.component';
import { BankTransactionTypeRoutingResolveService } from './bank-transaction-type-routing-resolve.service';

const bankTransactionTypeRoute: Routes = [
  {
    path: '',
    component: BankTransactionTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankTransactionTypeDetailComponent,
    resolve: {
      bankTransactionType: BankTransactionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankTransactionTypeUpdateComponent,
    resolve: {
      bankTransactionType: BankTransactionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankTransactionTypeUpdateComponent,
    resolve: {
      bankTransactionType: BankTransactionTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bankTransactionTypeRoute)],
  exports: [RouterModule],
})
export class BankTransactionTypeRoutingModule {}
