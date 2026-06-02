import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaxRuleComponent } from '../list/tax-rule.component';
import { TaxRuleDetailComponent } from '../detail/tax-rule-detail.component';
import { TaxRuleUpdateComponent } from '../update/tax-rule-update.component';
import { TaxRuleRoutingResolveService } from './tax-rule-routing-resolve.service';

const taxRuleRoute: Routes = [
  {
    path: '',
    component: TaxRuleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaxRuleDetailComponent,
    resolve: {
      taxRule: TaxRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaxRuleUpdateComponent,
    resolve: {
      taxRule: TaxRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaxRuleUpdateComponent,
    resolve: {
      taxRule: TaxRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taxRuleRoute)],
  exports: [RouterModule],
})
export class TaxRuleRoutingModule {}
