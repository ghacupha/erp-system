import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionAccountPostingProcessTypeComponent } from '../list/transaction-account-posting-process-type.component';
import { TransactionAccountPostingProcessTypeDetailComponent } from '../detail/transaction-account-posting-process-type-detail.component';
import { TransactionAccountPostingProcessTypeUpdateComponent } from '../update/transaction-account-posting-process-type-update.component';
import { TransactionAccountPostingProcessTypeRoutingResolveService } from './transaction-account-posting-process-type-routing-resolve.service';

const transactionAccountPostingProcessTypeRoute: Routes = [
  {
    path: '',
    component: TransactionAccountPostingProcessTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountPostingProcessTypeDetailComponent,
    resolve: {
      transactionAccountPostingProcessType: TransactionAccountPostingProcessTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionAccountPostingProcessTypeUpdateComponent,
    resolve: {
      transactionAccountPostingProcessType: TransactionAccountPostingProcessTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionAccountPostingProcessTypeUpdateComponent,
    resolve: {
      transactionAccountPostingProcessType: TransactionAccountPostingProcessTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionAccountPostingProcessTypeRoute)],
  exports: [RouterModule],
})
export class TransactionAccountPostingProcessTypeRoutingModule {}
