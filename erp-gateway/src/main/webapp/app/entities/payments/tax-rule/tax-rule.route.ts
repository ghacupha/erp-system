///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITaxRule, TaxRule } from 'app/shared/model/payments/tax-rule.model';
import { TaxRuleService } from './tax-rule.service';
import { TaxRuleComponent } from './tax-rule.component';
import { TaxRuleDetailComponent } from './tax-rule-detail.component';
import { TaxRuleUpdateComponent } from './tax-rule-update.component';

@Injectable({ providedIn: 'root' })
export class TaxRuleResolve implements Resolve<ITaxRule> {
  constructor(private service: TaxRuleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((taxRule: HttpResponse<TaxRule>) => {
          if (taxRule.body) {
            return of(taxRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaxRule());
  }
}

export const taxRuleRoute: Routes = [
  {
    path: '',
    component: TaxRuleComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TaxRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaxRuleDetailComponent,
    resolve: {
      taxRule: TaxRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TaxRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaxRuleUpdateComponent,
    resolve: {
      taxRule: TaxRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TaxRules',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaxRuleUpdateComponent,
    resolve: {
      taxRule: TaxRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TaxRules',
    },
    canActivate: [UserRouteAccessService],
  },
];