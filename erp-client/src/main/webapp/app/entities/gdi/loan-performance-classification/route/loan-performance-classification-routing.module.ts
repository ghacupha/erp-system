import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LoanPerformanceClassificationComponent } from '../list/loan-performance-classification.component';
import { LoanPerformanceClassificationDetailComponent } from '../detail/loan-performance-classification-detail.component';
import { LoanPerformanceClassificationUpdateComponent } from '../update/loan-performance-classification-update.component';
import { LoanPerformanceClassificationRoutingResolveService } from './loan-performance-classification-routing-resolve.service';

const loanPerformanceClassificationRoute: Routes = [
  {
    path: '',
    component: LoanPerformanceClassificationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LoanPerformanceClassificationDetailComponent,
    resolve: {
      loanPerformanceClassification: LoanPerformanceClassificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LoanPerformanceClassificationUpdateComponent,
    resolve: {
      loanPerformanceClassification: LoanPerformanceClassificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LoanPerformanceClassificationUpdateComponent,
    resolve: {
      loanPerformanceClassification: LoanPerformanceClassificationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(loanPerformanceClassificationRoute)],
  exports: [RouterModule],
})
export class LoanPerformanceClassificationRoutingModule {}
