import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanRepaymentFrequencyComponent } from '../list/loan-repayment-frequency.component';
import { LoanRepaymentFrequencyDetailComponent } from '../detail/loan-repayment-frequency-detail.component';
import { LoanRepaymentFrequencyUpdateComponent } from '../update/loan-repayment-frequency-update.component';
import { LoanRepaymentFrequencyRoutingResolveService } from './loan-repayment-frequency-routing-resolve.service';

const loanRepaymentFrequencyRoute: Routes = [
  {
    path: '',
    component: LoanRepaymentFrequencyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanRepaymentFrequencyDetailComponent,
    resolve: {
      loanRepaymentFrequency: LoanRepaymentFrequencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanRepaymentFrequencyUpdateComponent,
    resolve: {
      loanRepaymentFrequency: LoanRepaymentFrequencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanRepaymentFrequencyUpdateComponent,
    resolve: {
      loanRepaymentFrequency: LoanRepaymentFrequencyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanRepaymentFrequencyRoute)],
  exports: [RouterModule],
})
export class LoanRepaymentFrequencyRoutingModule {}
