import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanRestructureFlagComponent } from '../list/loan-restructure-flag.component';
import { LoanRestructureFlagDetailComponent } from '../detail/loan-restructure-flag-detail.component';
import { LoanRestructureFlagUpdateComponent } from '../update/loan-restructure-flag-update.component';
import { LoanRestructureFlagRoutingResolveService } from './loan-restructure-flag-routing-resolve.service';

const loanRestructureFlagRoute: Routes = [
  {
    path: '',
    component: LoanRestructureFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanRestructureFlagDetailComponent,
    resolve: {
      loanRestructureFlag: LoanRestructureFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanRestructureFlagUpdateComponent,
    resolve: {
      loanRestructureFlag: LoanRestructureFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanRestructureFlagUpdateComponent,
    resolve: {
      loanRestructureFlag: LoanRestructureFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanRestructureFlagRoute)],
  exports: [RouterModule],
})
export class LoanRestructureFlagRoutingModule {}
