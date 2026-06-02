import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TALeaseInterestAccrualRuleComponent } from '../list/ta-lease-interest-accrual-rule.component';
import { TALeaseInterestAccrualRuleDetailComponent } from '../detail/ta-lease-interest-accrual-rule-detail.component';
import { TALeaseInterestAccrualRuleUpdateComponent } from '../update/ta-lease-interest-accrual-rule-update.component';
import { TALeaseInterestAccrualRuleRoutingResolveService } from './ta-lease-interest-accrual-rule-routing-resolve.service';

const tALeaseInterestAccrualRuleRoute: Routes = [
  {
    path: '',
    component: TALeaseInterestAccrualRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TALeaseInterestAccrualRuleDetailComponent,
    resolve: {
      tALeaseInterestAccrualRule: TALeaseInterestAccrualRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TALeaseInterestAccrualRuleUpdateComponent,
    resolve: {
      tALeaseInterestAccrualRule: TALeaseInterestAccrualRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TALeaseInterestAccrualRuleUpdateComponent,
    resolve: {
      tALeaseInterestAccrualRule: TALeaseInterestAccrualRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tALeaseInterestAccrualRuleRoute)],
  exports: [RouterModule],
})
export class TALeaseInterestAccrualRuleRoutingModule {}
