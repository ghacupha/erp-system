import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanProductTypeComponent } from '../list/loan-product-type.component';
import { LoanProductTypeDetailComponent } from '../detail/loan-product-type-detail.component';
import { LoanProductTypeUpdateComponent } from '../update/loan-product-type-update.component';
import { LoanProductTypeRoutingResolveService } from './loan-product-type-routing-resolve.service';

const loanProductTypeRoute: Routes = [
  {
    path: '',
    component: LoanProductTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanProductTypeDetailComponent,
    resolve: {
      loanProductType: LoanProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanProductTypeUpdateComponent,
    resolve: {
      loanProductType: LoanProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanProductTypeUpdateComponent,
    resolve: {
      loanProductType: LoanProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanProductTypeRoute)],
  exports: [RouterModule],
})
export class LoanProductTypeRoutingModule {}
