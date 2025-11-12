///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
      defaultSort: 'id,desc',
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
