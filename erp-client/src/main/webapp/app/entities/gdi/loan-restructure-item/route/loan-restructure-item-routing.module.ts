import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanRestructureItemComponent } from '../list/loan-restructure-item.component';
import { LoanRestructureItemDetailComponent } from '../detail/loan-restructure-item-detail.component';
import { LoanRestructureItemUpdateComponent } from '../update/loan-restructure-item-update.component';
import { LoanRestructureItemRoutingResolveService } from './loan-restructure-item-routing-resolve.service';

const loanRestructureItemRoute: Routes = [
  {
    path: '',
    component: LoanRestructureItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanRestructureItemDetailComponent,
    resolve: {
      loanRestructureItem: LoanRestructureItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanRestructureItemUpdateComponent,
    resolve: {
      loanRestructureItem: LoanRestructureItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanRestructureItemUpdateComponent,
    resolve: {
      loanRestructureItem: LoanRestructureItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanRestructureItemRoute)],
  exports: [RouterModule],
})
export class LoanRestructureItemRoutingModule {}
