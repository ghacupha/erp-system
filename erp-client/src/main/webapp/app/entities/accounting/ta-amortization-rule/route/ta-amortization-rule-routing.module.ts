import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TAAmortizationRuleComponent } from '../list/ta-amortization-rule.component';
import { TAAmortizationRuleDetailComponent } from '../detail/ta-amortization-rule-detail.component';
import { TAAmortizationRuleUpdateComponent } from '../update/ta-amortization-rule-update.component';
import { TAAmortizationRuleRoutingResolveService } from './ta-amortization-rule-routing-resolve.service';

const tAAmortizationRuleRoute: Routes = [
  {
    path: '',
    component: TAAmortizationRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TAAmortizationRuleDetailComponent,
    resolve: {
      tAAmortizationRule: TAAmortizationRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TAAmortizationRuleUpdateComponent,
    resolve: {
      tAAmortizationRule: TAAmortizationRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TAAmortizationRuleUpdateComponent,
    resolve: {
      tAAmortizationRule: TAAmortizationRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tAAmortizationRuleRoute)],
  exports: [RouterModule],
})
export class TAAmortizationRuleRoutingModule {}
