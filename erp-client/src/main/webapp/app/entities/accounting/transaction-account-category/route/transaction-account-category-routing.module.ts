import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionAccountCategoryComponent } from '../list/transaction-account-category.component';
import { TransactionAccountCategoryDetailComponent } from '../detail/transaction-account-category-detail.component';
import { TransactionAccountCategoryUpdateComponent } from '../update/transaction-account-category-update.component';
import { TransactionAccountCategoryRoutingResolveService } from './transaction-account-category-routing-resolve.service';

const transactionAccountCategoryRoute: Routes = [
  {
    path: '',
    component: TransactionAccountCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountCategoryDetailComponent,
    resolve: {
      transactionAccountCategory: TransactionAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionAccountCategoryUpdateComponent,
    resolve: {
      transactionAccountCategory: TransactionAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionAccountCategoryUpdateComponent,
    resolve: {
      transactionAccountCategory: TransactionAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionAccountCategoryRoute)],
  exports: [RouterModule],
})
export class TransactionAccountCategoryRoutingModule {}
