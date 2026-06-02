import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanApplicationTypeComponent } from '../list/loan-application-type.component';
import { LoanApplicationTypeDetailComponent } from '../detail/loan-application-type-detail.component';
import { LoanApplicationTypeUpdateComponent } from '../update/loan-application-type-update.component';
import { LoanApplicationTypeRoutingResolveService } from './loan-application-type-routing-resolve.service';

const loanApplicationTypeRoute: Routes = [
  {
    path: '',
    component: LoanApplicationTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanApplicationTypeDetailComponent,
    resolve: {
      loanApplicationType: LoanApplicationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanApplicationTypeUpdateComponent,
    resolve: {
      loanApplicationType: LoanApplicationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanApplicationTypeUpdateComponent,
    resolve: {
      loanApplicationType: LoanApplicationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanApplicationTypeRoute)],
  exports: [RouterModule],
})
export class LoanApplicationTypeRoutingModule {}
