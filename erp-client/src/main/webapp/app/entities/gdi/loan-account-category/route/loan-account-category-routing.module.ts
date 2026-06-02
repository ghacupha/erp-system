import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanAccountCategoryComponent } from '../list/loan-account-category.component';
import { LoanAccountCategoryDetailComponent } from '../detail/loan-account-category-detail.component';
import { LoanAccountCategoryUpdateComponent } from '../update/loan-account-category-update.component';
import { LoanAccountCategoryRoutingResolveService } from './loan-account-category-routing-resolve.service';

const loanAccountCategoryRoute: Routes = [
  {
    path: '',
    component: LoanAccountCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanAccountCategoryDetailComponent,
    resolve: {
      loanAccountCategory: LoanAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanAccountCategoryUpdateComponent,
    resolve: {
      loanAccountCategory: LoanAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanAccountCategoryUpdateComponent,
    resolve: {
      loanAccountCategory: LoanAccountCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanAccountCategoryRoute)],
  exports: [RouterModule],
})
export class LoanAccountCategoryRoutingModule {}
