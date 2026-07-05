import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanApplicationStatusComponent } from '../list/loan-application-status.component';
import { LoanApplicationStatusDetailComponent } from '../detail/loan-application-status-detail.component';
import { LoanApplicationStatusUpdateComponent } from '../update/loan-application-status-update.component';
import { LoanApplicationStatusRoutingResolveService } from './loan-application-status-routing-resolve.service';

const loanApplicationStatusRoute: Routes = [
  {
    path: '',
    component: LoanApplicationStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanApplicationStatusDetailComponent,
    resolve: {
      loanApplicationStatus: LoanApplicationStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanApplicationStatusUpdateComponent,
    resolve: {
      loanApplicationStatus: LoanApplicationStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanApplicationStatusUpdateComponent,
    resolve: {
      loanApplicationStatus: LoanApplicationStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanApplicationStatusRoute)],
  exports: [RouterModule],
})
export class LoanApplicationStatusRoutingModule {}
