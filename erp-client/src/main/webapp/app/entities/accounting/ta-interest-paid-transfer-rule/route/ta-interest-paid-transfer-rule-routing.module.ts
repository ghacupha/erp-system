import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TAInterestPaidTransferRuleComponent } from '../list/ta-interest-paid-transfer-rule.component';
import { TAInterestPaidTransferRuleDetailComponent } from '../detail/ta-interest-paid-transfer-rule-detail.component';
import { TAInterestPaidTransferRuleUpdateComponent } from '../update/ta-interest-paid-transfer-rule-update.component';
import { TAInterestPaidTransferRuleRoutingResolveService } from './ta-interest-paid-transfer-rule-routing-resolve.service';

const tAInterestPaidTransferRuleRoute: Routes = [
  {
    path: '',
    component: TAInterestPaidTransferRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TAInterestPaidTransferRuleDetailComponent,
    resolve: {
      tAInterestPaidTransferRule: TAInterestPaidTransferRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TAInterestPaidTransferRuleUpdateComponent,
    resolve: {
      tAInterestPaidTransferRule: TAInterestPaidTransferRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TAInterestPaidTransferRuleUpdateComponent,
    resolve: {
      tAInterestPaidTransferRule: TAInterestPaidTransferRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tAInterestPaidTransferRuleRoute)],
  exports: [RouterModule],
})
export class TAInterestPaidTransferRuleRoutingModule {}
