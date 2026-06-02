import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TALeaseRepaymentRuleComponent } from '../list/ta-lease-repayment-rule.component';
import { TALeaseRepaymentRuleDetailComponent } from '../detail/ta-lease-repayment-rule-detail.component';
import { TALeaseRepaymentRuleUpdateComponent } from '../update/ta-lease-repayment-rule-update.component';
import { TALeaseRepaymentRuleRoutingResolveService } from './ta-lease-repayment-rule-routing-resolve.service';

const tALeaseRepaymentRuleRoute: Routes = [
  {
    path: '',
    component: TALeaseRepaymentRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TALeaseRepaymentRuleDetailComponent,
    resolve: {
      tALeaseRepaymentRule: TALeaseRepaymentRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TALeaseRepaymentRuleUpdateComponent,
    resolve: {
      tALeaseRepaymentRule: TALeaseRepaymentRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TALeaseRepaymentRuleUpdateComponent,
    resolve: {
      tALeaseRepaymentRule: TALeaseRepaymentRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tALeaseRepaymentRuleRoute)],
  exports: [RouterModule],
})
export class TALeaseRepaymentRuleRoutingModule {}
