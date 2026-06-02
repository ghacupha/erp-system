import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanDeclineReasonComponent } from '../list/loan-decline-reason.component';
import { LoanDeclineReasonDetailComponent } from '../detail/loan-decline-reason-detail.component';
import { LoanDeclineReasonUpdateComponent } from '../update/loan-decline-reason-update.component';
import { LoanDeclineReasonRoutingResolveService } from './loan-decline-reason-routing-resolve.service';

const loanDeclineReasonRoute: Routes = [
  {
    path: '',
    component: LoanDeclineReasonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanDeclineReasonDetailComponent,
    resolve: {
      loanDeclineReason: LoanDeclineReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanDeclineReasonUpdateComponent,
    resolve: {
      loanDeclineReason: LoanDeclineReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanDeclineReasonUpdateComponent,
    resolve: {
      loanDeclineReason: LoanDeclineReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanDeclineReasonRoute)],
  exports: [RouterModule],
})
export class LoanDeclineReasonRoutingModule {}
